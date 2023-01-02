package com.jake.blog.controller.api;

import com.jake.blog.config.auth.PrincipalDetail;
import com.jake.blog.domain.Board;
import com.jake.blog.dto.ResponseDto;
import com.jake.blog.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {
    private final BoardService boardService;

    public BoardApiController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(
            @RequestBody Board board,
            @AuthenticationPrincipal PrincipalDetail principal
    ) {
        Integer result = boardService.writeBoard(board, principal.getMember());
        return new ResponseDto<>(HttpStatus.OK.value(), result);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable Long id, @RequestBody Board board) {
        boardService.updateBoard(id, board);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
