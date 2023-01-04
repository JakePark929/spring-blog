package com.jake.blog.domain;

import com.jake.blog.dto.ReplySaveRequestDto;
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

    @Setter
    @ManyToOne // Many = Reply to One = board
    @JoinColumn(name = "boardId")
    private Board board;

    @Setter
    @ManyToOne // Many Reply to One User
    @JoinColumn(name = "memberId")
    private Member member;

    @Setter
    @CreationTimestamp
    private Timestamp createDate;

    public void update(Member member, Board board, String content) {
        setMember(member);
        setBoard(board);
        setContent(content);
    }
}
