package com.jake.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스피링이 com.jake.blog 패키지 이하를 스캔해서 모든 파일을 new 하는 것이 아닌
// 특정 어노테이션이 붙어있는 클래스 파일을 new 해서 스프링 컨테이너에 관리해줌
@RestController
public class BlogControllerTest {
    @GetMapping("/test/hello")
    public String hello() {
        return "<h1>hello spring boot<h1/>";
    }
}
