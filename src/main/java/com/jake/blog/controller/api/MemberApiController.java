package com.jake.blog.controller.api;

import com.jake.blog.domain.Member;
import com.jake.blog.dto.ResponseDto;
import com.jake.blog.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
//    private final HttpSession session;

    public MemberApiController(MemberService memberService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody Member member) {
        System.out.println("MemberApiController.save 호출됨");
        int result = memberService.signUp(member);
        return new ResponseDto<>(HttpStatus.OK.value(), result); //java object 를 json 으로 리턴(Jackson)
    }

    @PutMapping("/user")
    public ResponseDto<Integer> update(
            @RequestBody Member member
//            @AuthenticationPrincipal PrincipalDetail principal,
//            HttpSession session
    ) { // key=value, x-www-form-urlencoded
        Member newMem = memberService.updateMember(member);
        // 여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됬음
        // 하지만 세션값은 변경되지 않은 상태이기 때문에 직접 세션값을 변경해줘야 함
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//        SecurityContext securityContext = SecurityContextHolder.getContext(); // 강제로 세션 값 바꾸기
//        securityContext.setAuthentication(authentication);
//        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext); // 막힘
        // 세션 등록
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    // session 방식 login/logout 구현
    // session.removeAttribute("key"); // key 삭제
    // session.invalidate(); //세션의 모든 속성을 삭제
//    @PostMapping("/api/member/login")
////    public ResponseDto<Integer> login(@RequestBody Member member, HttpSession session) {
//    public ResponseDto<Integer> login(@RequestBody Member member) {
//        System.out.println("MemberApiController.login 호출됨");
//        Member principal = memberService.login(member); // principal 접근주체
//        System.out.println("principal = " + principal);
//        if(principal != null) {
//            session.setAttribute("principal", principal);
//        }
//        return new ResponseDto<>(HttpStatus.OK.value(), 1); //java object 를 json 으로 리턴(Jackson)
//    }
}
