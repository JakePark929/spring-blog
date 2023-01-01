package com.jake.blog.controller.api;

import com.jake.blog.domain.Member;
import com.jake.blog.domain.RoleType;
import com.jake.blog.dto.ResponseDto;
import com.jake.blog.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {
    private final MemberService memberService;
//    private final HttpSession session;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody Member member) {
        System.out.println("MemberApiController.save 호출됨");
        /// 실제로 DB에 insert 하고 아래에서 return
        member.setRole(RoleType.USER);
        int result = memberService.signUp(member);
        return new ResponseDto<>(HttpStatus.OK.value(), result); //java object 를 json 으로 리턴(Jackson)
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
