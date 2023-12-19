package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostNoAndStatus(Long postNo, StatusType status);

    Optional<Post> findByPostNoAndBoardTypeAndRoleAndStatus(Long postNo, BoardType boardType, MemberRole role, StatusType statusType);
}
