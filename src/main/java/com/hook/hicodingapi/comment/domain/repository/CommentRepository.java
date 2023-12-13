package com.hook.hicodingapi.comment.domain.repository;

import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.type.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByCmtNoAndStatus(final Long cmtNo, final StatusType status);
}
