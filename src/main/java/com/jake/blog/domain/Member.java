package com.jake.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// ORM -> Java(다른 언어) Object -> 테이블로 매핑해주는 기술
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // insert 시에 null 을 제외시켜줌
@Entity // User 클래스가 MySQL 에 테이블이 생성이 된다.
public class Member {
    @Id // PrimaryKey
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라 간다.
    private Long id; // 시퀀스, auto_increment
    @Setter @Column(nullable = false, length = 30, unique = true) private String username; // 이메일
    @Setter @Column(nullable = false, length = 100) private String password; // 해쉬로 변경하여 암호화 예정
    @Setter @Column(nullable = false, length = 50) private String email; // springPhysical 전략 - snake_case 로 변경, 그냥 physical - camelCase

//    @ColumnDefault("'user'")
    // DB는 RoleType 이라는 게 없다
    @Enumerated(EnumType.STRING)
    @Setter private RoleType role; // Enum 을 쓰는게 좋다. admin, user, manager - 데이터의 도메인(범위)을 만들 수 있음, 오타를 낼 문제가 있음

    @CreationTimestamp // 시간이 자동 입력
    private Timestamp createDate;

    @Builder
    public Member(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void setMemberId(Long id) {
        this.id = id;
    }
}
