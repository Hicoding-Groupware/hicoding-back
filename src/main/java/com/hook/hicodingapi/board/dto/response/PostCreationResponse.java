package com.hook.hicodingapi.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Getter
public class PostCreationResponse {
    private Long no;
    private String title;
    private String content;
    private int views;
    private int likesCnt;
    private boolean isPublic;
    private boolean isNoticePost;

    @Enumerated(value = STRING)
    private StatusType status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    private Member writer;
    private Long parentNo;

    public static PostCreationResponse from(final Post newPost) {
        return new PostCreationResponse(
                newPost.getPostNo(),
                newPost.getPostTitle(),
                newPost.getPostContent(),
                newPost.getViews(),
                newPost.getLikesCount(),
                newPost.isPublic(),
                newPost.isNoticePost(),
                newPost.getStatus(),
                newPost.getCreatedAt(),
                newPost.getWriter(),
                newPost.getParent().getPostNo()
        );
    }
}
