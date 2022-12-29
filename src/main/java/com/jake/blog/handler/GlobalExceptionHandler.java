package com.jake.blog.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 어디에서든 Exception 을 받으려면
@RestController
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class)
//    public String handleException(IllegalArgumentException e) {
//        return "<h1>" + "Exception" + "</h1>";
//    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleArgumentException(IllegalArgumentException e) {
        return "<h1>" + e.getMessage() + "</h1>";
    }
}
