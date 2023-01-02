package com.jake.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration // 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@EnableWebSecurity // 시큐리티 필터추가 = 스프링 시큐리티가 활성화가 되어 있는데 어던 설정을 해당 파일에서 하겠다.
@EnableMethodSecurity(prePostEnabled = true) // 특정주소로 접근을 하면 권한 및 인증을 미리체크 하겠다
public class SecurityConfig {
    // 시큐리티가 대신 로그인 해주는데 password 를 가로채는데
    // 해당 password 가 뭘로 해쉬되어 회원가입 되어있는지 알아야
    // 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()// csrf 토큰 비활성화(테스트 시 걸어두는게 좋음) - csrf 토큰없이 POST 날리면 막힘
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/" , "/auth/**",
                                "/js/**" ,"/css/**" ,"/image",
                                "/dummy/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                    .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다.
                .defaultSuccessUrl("/")
//                .failureUrl("/fail") // login fail 시
                .and()
                .build();
    }

    // 없어짐?
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(null).passwordEncoder(encodePwd());
//    }

    @Bean // IoC가 됨!
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }
}
