package com.jake.blog.repository;

import com.jake.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// DAO 같은 역할
// Bean 으로 등록 되나? 자동으로 Bean 으로 등록됨 -> @Repository 생략가능 
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    // Jpa Naming 쿼리
    // SELECT * FROM user WHERE username = ?1 AND password =?2;
//    Member findByUsernameAndPassword(String username, String password);

//    @Query(value = "SELECT * FROM user WHERE username = ?1 AND password =?2", nativeQuery = true)
//    Member login(String username, String password);
}
