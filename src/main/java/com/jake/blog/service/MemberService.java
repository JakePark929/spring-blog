package com.jake.blog.service;

import com.jake.blog.domain.Member;
import com.jake.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean 에 등록을 해줌. IoC를 해준다.
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Integer signUp(Member member) {
        try {
            memberRepository.save(member);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MemberService.signUp: " + e.getMessage());
        }
        return -1;
    }
}
