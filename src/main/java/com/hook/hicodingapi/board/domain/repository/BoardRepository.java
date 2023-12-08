package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Board;
import com.hook.hicodingapi.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
