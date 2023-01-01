package com.jake.blog.service;

import com.jake.blog.domain.Member;
import com.jake.blog.domain.RoleType;
import com.jake.blog.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean 에 등록을 해줌. IoC를 해준다.
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    @Transactional
    public Integer signUp(Member member) {
        try {
            // 실제로 DB에 insert 하고 아래에서 return
            String rawPassword = member.getPassword();// 1234 원문
            String encPassword = encoder.encode(rawPassword);// 1234 원문
            member.setPassword(encPassword);
            member.setRole(RoleType.USER);
            memberRepository.save(member);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MemberService.signUp: " + e.getMessage());
        }
        return -1;
    }

//    @Transactional(readOnly = true) // select 할 때 transaction 시작, 서비스 종료시에 트랜잭션 종료 (정합성)
//    public Member login(Member member) {
//        return memberRepository.findByUsernameAndPassword(member.getUsername(), member.getPassword());
//    }
}
