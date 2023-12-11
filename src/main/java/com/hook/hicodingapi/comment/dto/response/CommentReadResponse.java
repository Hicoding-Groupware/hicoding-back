package com.hook.hicodingapi.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;
import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Getter
@Setter
public class CommentReadResponse {
    private Long no;
    private String content;

    @Enumerated(value = STRING)
    private StatusType status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    private Long postNo;
    private Member writer;
    private List<CommentReadResponse> childrenList;

    public static CommentReadResponse from(final Comment comment) {
        return new CommentReadResponse(
                comment.getCmtNo(),
                comment.getCmtContent(),
                comment.getStatus(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getPostCode().getPostNo(),
                comment.getWriter(),
                null
        );
    }
}
