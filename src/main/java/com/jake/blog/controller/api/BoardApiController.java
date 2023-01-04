package com.jake.blog.controller.api;

import com.jake.blog.config.auth.PrincipalDetail;
import com.jake.blog.domain.Board;
import com.jake.blog.dto.ReplySaveRequestDto;
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

    // 데이터를 받을 때 컨트롤러에서 dto 를 만들어서 받는게 좋다.
    // dto 를 사용하지 않은 이유는! 데이터 이동이 필요없어짐
    // filter 가 많이 추가되는데 model 이 그냥 받으면 좋지 않음
    @PostMapping("/api/board/{boardId}/reply")
//    public ResponseDto<Integer> replySave(
//            @PathVariable Long boardId,
//            @RequestBody Reply reply,
//            @AuthenticationPrincipal PrincipalDetail principal
//    ) {
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        Integer result = boardService.writeBoardReply(replySaveRequestDto);
        return new ResponseDto<>(HttpStatus.OK.value(), result);
    }

    @DeleteMapping("api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable Long replyId) {
        boardService.deleteReply(replyId);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
