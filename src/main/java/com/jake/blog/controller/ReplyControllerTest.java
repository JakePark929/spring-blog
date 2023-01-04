package com.jake.blog.controller;

import com.jake.blog.domain.Board;
import com.jake.blog.domain.Reply;
import com.jake.blog.repository.BoardRepository;
import com.jake.blog.repository.ReplyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReplyControllerTest {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public ReplyControllerTest(BoardRepository boardRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
    }

    @GetMapping("/test/board/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardRepository.findById(id).get(); // jackson 라이브러리(오브젝트를 json 으로 리턴) -> 모델의 getter 호출
    }

    @GetMapping("/test/reply")
    public List<Reply> getReply() {
        return replyRepository.findAll();
    }
}
