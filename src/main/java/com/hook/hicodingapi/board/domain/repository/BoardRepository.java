package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardRole;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.common.domain.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostNoAndStatus(Long postNo, StatusType status);

    Optional<Post> findByPostNoAndBoardTypeAndStatus(Long postNo, BoardType boardType, StatusType statusType);

    Optional<Post> findByPostNoAndBoardTypeAndRoleAndStatus(Long postNo, BoardType boardType, BoardRole role, StatusType statusType);
}
