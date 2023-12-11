package com.hook.hicodingapi.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Getter
@Setter
public class CommentCreationResponse {

    final private Long no;
    final private String content;
    final private Member writer;
    final private Long parentNo;

    @Enumerated(value = STRING)
    protected StatusType status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public static CommentCreationResponse from(final Comment newComment) {
        final Comment parent = newComment.getParent();

        return new CommentCreationResponse(
                newComment.getCmtNo(),
                newComment.getCmtContent(),
                newComment.getWriter(),
                parent == null ? null : parent.getCmtNo(),
                newComment.getStatus(),
                newComment.getCreatedAt()
        );
    }
}
