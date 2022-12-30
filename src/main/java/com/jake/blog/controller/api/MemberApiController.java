package com.jake.blog.controller.api;

import com.jake.blog.domain.Member;
import com.jake.blog.domain.RoleType;
import com.jake.blog.dto.ResponseDto;
import com.jake.blog.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/member")
    public ResponseDto<Integer> save(@RequestBody Member member) {
        System.out.println("MemberApiController: save 호출됨");
        /// 실제로 DB에 insert 하고 아래에서 return
        member.setRole(RoleType.USER);
        int result = memberService.signUp(member);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), result); //java object 를 json 으로 리턴(Jackson)
    }
}
