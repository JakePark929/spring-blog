package com.jake.blog.controller;

import com.jake.blog.domain.Member;
import com.jake.blog.domain.RoleType;
import com.jake.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController // html 파일이 아니라 data 를 리턴해주는 controller
public class DummyControllerTest {
    private final MemberRepository memberRepository;

    public DummyControllerTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // {id} 주소로 파라미터를 전달 받을 수 잇음
    // http://localhost:8080/blog/dummy/user/3
    @GetMapping("/dummy/member/{id}")
    public Member detail(@PathVariable Long id) {
        // user/4 를 찾으면 내가 DB 에서 못 찾아 오게 되면 user 가 null 이 될 것
        // 그럼 return null 이 리턴 됨.. 문제가 있지 않니?
        // Optional 로 너의객체를 감싸서 가져올테니 null 인지 아닌지 판단해서 return 해
        // .get 은 위험함..
//        Member member = memberRepository.findById(id).get(); 
        // orElseGet return
//        Member member = memberRepository.findById(id).orElseGet(new Supplier<Member>() {
//            @Override
//            public Member get() {
//                return new Member();
//            }
//        });
        // orElseGet error return
//        Member member = memberRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//            @Override
//            public IllegalArgumentException get() {
//                return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
//            }
//        });
        // ★람다식★
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id: " + id));
        
        // 요청: 웹브라우저
        // user 객체는 java object -> 변환(웹 브라우저가 이해할 수 잇는 데이터) -> json(Gson 라이브러리)
        // 스프링부트 - MessageConverter 가 응답시에 자동 작동
        // -> java object 리턴시 MessageConverter 가 Jackson 라이브러리를 호출해서 user object 를 json 으로 변환 후 응답
        return member;
    }

    // http://localhost:8080/blog/dummy/user
    @GetMapping("/dummy/members")
    public List<Member> list() {
        return memberRepository.findAll();
    }

    @GetMapping("/dummy/member")
    public List<Member> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> pagingMembers = memberRepository.findAll(pageable);
//        if(pagingMembers.isLast()) {
//
//        }
        List<Member> members = pagingMembers.getContent();
        return members;
    }

    // http://localhost:8080/blog/dummy/join(요청)
    // http body 에 username, password, email 데이터를 가지고 옴 (요청)
    @PostMapping("/dummy/join")
//    public String join(String username, String password, String email) { // 변수명만 잘적어 주면 @RequestParam 필요없음 - key=value 로 받음(규칙)
    public String join(Member member) { // 변수명만 잘적어 주면 @RequestParam 필요없음 - key=value 로 받음(규칙)
//        System.out.println("username = " + username);
//        System.out.println("password = " + password);
//        System.out.println("email = " + email);
        System.out.println("member.getId() = " + member.getId());
        System.out.println("member.getUsername() = " + member.getUsername());
        System.out.println("member.getPassword() = " + member.getPassword());
        System.out.println("member.getEmail() = " + member.getEmail());
        System.out.println("member.getRole() = " + member.getRole());
        System.out.println("member.getCreateDate() = " + member.getCreateDate());

        member.setRole(RoleType.USER);
        memberRepository.save(member);
        return "회원가입이 완료되었습니다.";
    }
    
    // save 함수는 id 를 전달하지 않으면 insert 를 해주고
    // id 를 전달할때 해당 id에 대한 데이터가 있으면 update 를 하고
    // id 를 전달할때 해당 id에 대한 데이터가 없으면 insert 를 함
    // email, password
    @Transactional
    @PutMapping("/dummy/member/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member requestMember) { // json 데이터를 요청 => java object 로 변환해서 받아줌(MessageConverter 의 Jackson 라이브러리)
        System.out.println("id = " + id);
        System.out.println("reqMember.getPassword() = " + requestMember.getPassword());
        System.out.println("reqMember.getEmail() = " + requestMember.getEmail());
        
        // 호출해서 업데이트
        Member member = memberRepository.findById(id).orElseThrow(()->new IllegalArgumentException("수정에 실패하였습니다. id: " + id));
        member.setPassword(requestMember.getPassword());
        member.setEmail(requestMember.getEmail());
//        memberRepository.save(member);
        
        // save 만 할 때
//        requestMember.setMemberId(id);
//        memberRepository.save(requestMember); // 그냥 수정하면 null 로 됨

        // 더티 체킹!! - 영속화 컨텍스트에 영속화된 객체를 확인하여 다시 flush 함
        return member;
    }

    @DeleteMapping("/dummy/member/{id}")
    public String delete(@PathVariable Long id) {
        try {
            memberRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
        }
        return "삭제되었습니다 id: " + id;
    }
}
