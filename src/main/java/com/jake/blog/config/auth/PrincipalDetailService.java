package com.jake.blog.config.auth;

import com.jake.blog.domain.Member;
import com.jake.blog.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public PrincipalDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
    // password 부분처리는 알아서 함
    // username 이 DB에 있는지만 확인해 주면 됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member principal = memberRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.: "+username));
        return new PrincipalDetail(principal); // 시큐리티의 세션에 유저 정보가 저장됨
    }
}
