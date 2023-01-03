package com.jake.blog.service;

import com.jake.blog.domain.Member;
import com.jake.blog.model.RoleType;
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

    @Transactional(readOnly = true)
    public Member findMember(String username) {
        Member member = memberRepository.findByUsername(username).orElseGet(()->new Member());
        return member;
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

    @Transactional
    public Member updateMember(Member requestMember) {
        // 수정시에는 영속성 컨텍스트에 User 오브젝트를 영속화 시키고, 영속화된 User 오브젝트를 수정
        // select 를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해서
        // 영속화된 오브젝트를 변경하면 자동으로 DB 에 update 문을 날려줌
            Member member = memberRepository.findById(requestMember.getId())
                    .orElseThrow(() -> new IllegalArgumentException("회원찾기 실패"));
        if (requestMember.getPassword() != "") {
            String rawPassword = requestMember.getPassword();
            String encPassword = encoder.encode(rawPassword);
            member.setPassword(encPassword);
        }
        if (requestMember.getEmail() != "") {
            member.setEmail(requestMember.getEmail());
        }
        // 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit 이 됨
        // 영속화된 persistence 객체의 변화가 감지되면 더티체킹이 되어 update 문을 날려줌
        return member;
    }

//    @Transactional(readOnly = true) // select 할 때 transaction 시작, 서비스 종료시에 트랜잭션 종료 (정합성)
//    public Member login(Member member) {
//        return memberRepository.findByUsernameAndPassword(member.getUsername(), member.getPassword());
//    }
}
