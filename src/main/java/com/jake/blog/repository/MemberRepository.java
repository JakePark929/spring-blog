package com.jake.blog.repository;

import com.jake.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO 같은 역할
// Bean 으로 등록 되나? 자동으로 Bean 으로 등록됨 -> @Repository 생략가능 
public interface MemberRepository extends JpaRepository<Member, Long> {
}
