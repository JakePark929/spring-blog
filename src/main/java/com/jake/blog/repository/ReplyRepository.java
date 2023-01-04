package com.jake.blog.repository;

import com.jake.blog.domain.Reply;
import com.jake.blog.dto.ReplySaveRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
//    @Transactional
    @Modifying
    @Query(value = "insert into reply(memberId, boardId, content, createDate) values(?1, ?2, ?3, now())", nativeQuery = true)
    Reply mySave(ReplySaveRequestDto replySaveRequestDto); // interface 안에서는 public 생략가능, 업데이트된 행의 개수를 리턴해줌.
}
