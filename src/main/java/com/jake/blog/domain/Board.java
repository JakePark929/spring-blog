package com.jake.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Lob // 대용량 데이터 저장용
    @Column(length = 4024024)
    private String content; // 섬머노트 라이브러리 <html> 태그가 섞여서 디자인이 됨.

//    @ColumnDefault("0")
    @Setter private int count; // 조회수

    @Setter
    @ManyToOne(fetch = FetchType.EAGER) // Many = Board to One = Member, 1건 밖에 없기 때문에 바로가져오기
    @JoinColumn(name = "memberId")
    private Member member; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.

    // JoinColumn 은 foreign key 를 만들어 줌, mappedBy DB에 칼럼을 만들지 마세요
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // One = Board to Many Reply, 많기 때문에 Lazy Type(default)
    @JsonIgnoreProperties({"board"}) // ★ get 할 때 무한 참조를 방지해 줌
    @OrderBy("id desc")
    private List<Reply> replies;

    @CreationTimestamp
    private Timestamp createDate;
}
