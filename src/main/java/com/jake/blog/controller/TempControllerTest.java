package com.jake.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
    // http://localhost:8080/blog/temp/home
    @GetMapping("/temp/home")
    public String tempHome() {
        System.out.println("tempHome()");
        // 파일리턴 기본경로 : src/main/resources/static
        // 리턴명 : /home.html
        // 폴더경로 : src/main/resources/static/home.html
        return "home";
    }

    @GetMapping("/temp/img")
    public String tempImg() {
        return "/a.jpg";
    }
}
