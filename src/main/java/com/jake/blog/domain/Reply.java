package com.jake.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne // Many = Reply to One = board
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne // Many Reply to One User
    @JoinColumn(name = "memberId")
    private Member member;

    @CreationTimestamp
    private Timestamp createDate;
}
