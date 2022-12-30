package com.jake.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    @GetMapping("/member/joinForm")
    public String joinForm() {
        return "member/joinForm";
    }

    @GetMapping("/member/loginForm")
    public String loginForm() {
        return "member/loginForm";
    }
}
