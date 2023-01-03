package com.jake.blog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jake.blog.config.auth.PrincipalDetail;
import com.jake.blog.domain.Member;
import com.jake.blog.model.KakaoProfile;
import com.jake.blog.model.OAuthToken;
import com.jake.blog.service.BoardService;
import com.jake.blog.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 로 허용
// 그냥 주소가 / 이면 index 허용
// static 이하에 있는 /js/**. /css/** , image/** 허용
@Controller
public class MemberController {
    @Value("${cos.key}")
    private String cosKey;
    private final MemberService memberService;
    private final BoardService boardService;
    private final AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, BoardService boardService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "member/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "member/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(@AuthenticationPrincipal PrincipalDetail userInfo, Model model) {
        model.addAttribute("userInfo", userInfo);
        return "member/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code,
                                @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                                @AuthenticationPrincipal PrincipalDetail userInfo,
                                Model model) { // Data를 리턴해주는 컨트롤러 함수
        // POST 방식으로 key=value 데이터를 요청해야함(카카오쪽으로)
        // a tag 방식으로 못하니 get 방식...
        // 필요한 라이브러리 RestTemplate
        // 예전엔 HttpURLConnection url...
        // Retrofit2 (안드로이드에서 많이 씀)
        // OkHttp
        // Rest Http 라이브러리
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        // HttpBody 오브젝트 생성(변수화 시켜서 사용하는게 좋음)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "8d1f58fc4d7c4019159fe8b2ef7a7959");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);
        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        // Http 요청하기 - Post 방식으로 - response 변수의 응답 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // 요청 주소
                HttpMethod.POST, // 요청 메소드
                kakaoTokenRequest,
                String.class
        );

        // Gson, Json Simple, ObjectMapper - Json 을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("카카오 엑세스 토큰 = " + oAuthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers2);
        // Http 요청하기 - Post 방식으로 - response 변수의 응답 받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me", // 요청 주소
                HttpMethod.POST, // 요청 메소드
                kakaoProfileRequest,
                String.class
        );
        System.out.println(response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("카카오 아이디(번호): " + kakaoProfile.getId());
        System.out.println("카카오 이메일: " + kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임: " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("블로그서버 이메일: " + kakaoProfile.getKakao_account().getEmail());
//        UUID garbagePassword = UUID.randomUUID();
        // UUID 란 -> 중복되지 않는 어떤 특정 값을 만들어 내는 알고리즘
        System.out.println("블로그서버 패스워드: " + cosKey);

        Member kakaoMember = Member.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .password(cosKey)
                .email(kakaoProfile.getKakao_account().getEmail()).build();

        // 가입자 혹은 비가입자 체크해서 처리
        Member originMember = memberService.findMember(kakaoMember.getUsername());
        if (originMember.getUsername() == null) {
            System.out.println("기존 회원이 아닙니다!");
            memberService.signUp(kakaoMember);
        }
        // 로그인 처리
        System.out.println("자동 로그인을 진행합니다.");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoMember.getUsername(), cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("boards", boardService.listBoard(pageable));

        return "index";
    }
}
