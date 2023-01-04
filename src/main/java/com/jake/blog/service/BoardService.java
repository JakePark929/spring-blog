package com.jake.blog.service;

import com.jake.blog.domain.Board;
import com.jake.blog.domain.Member;
import com.jake.blog.domain.Reply;
import com.jake.blog.dto.ReplySaveRequestDto;
import com.jake.blog.repository.BoardRepository;
import com.jake.blog.repository.MemberRepository;
import com.jake.blog.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, ReplyRepository replyRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.replyRepository = replyRepository;
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
            return 1;
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

//    @Transactional
//    public Integer writeBoardReply(Member member, Long boardId, Reply requestReply) {
//        try {
//            Board board = boardRepository.findById(boardId)
//                    .orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다."));
//            requestReply.setMember(member);
//            requestReply.setBoard(board);
//            replyRepository.save(requestReply);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("BoardService.writeBoardReply: " + e.getMessage());
//        }
//        return -1;
//    }

    @Transactional
    public Integer writeBoardReply(ReplySaveRequestDto replySaveRequestDto) {
        try {
            Member member = memberRepository.findByUsername(replySaveRequestDto.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 유저 id를 찾을 수 없습니다."));
            Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
                    .orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다."));
            Reply reply = Reply.builder().member(member).board(board).content(replySaveRequestDto.getContent()).build();
//            Reply reply = new Reply();
//            reply.update(member, board, replySaveRequestDto.getContent());
            replyRepository.save(reply);
//            replyRepository.mySave(replySaveRequestDto);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BoardService.writeBoardReply: " + e.getMessage());
        }
        return -1;
    }

    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }
}
