package com.jake.blog.service;

import com.jake.blog.domain.Board;
import com.jake.blog.domain.Member;
import com.jake.blog.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public Page<Board> listBoard(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board detailBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다."));
    }

    @Transactional
    public Integer writeBoard(Board board, Member member) { // title, content
        try {
            board.setCount(0);
            board.setMember(member);
            boardRepository.save(board);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BoardService.writeBoard: " + e.getMessage());
        }
        return -1;
    }

    @Transactional
    public void updateBoard(Long id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.")); // 영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수 종료시 트랜잭션이 종료 된다. 이때 더티체킹 - 자동 업데이트 됨, db flush
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

}
