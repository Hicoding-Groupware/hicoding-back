package com.hook.hicodingapi.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.comment.dto.response.CommentReadResponse;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class PostReadResponse {

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;

    private Member writer;
    private Long parentNo;
    private List<PostReadResponse> childrenList;
    //private List<CommentReadResponse> commentList;

    public static PostReadResponse from(final Post newPost) {
        final Post parent = newPost.getParent();

        return new PostReadResponse(
                newPost.getPostNo(),
                newPost.getPostTitle(),
                newPost.getPostContent(),
                newPost.getViews(),
                newPost.getLikesCount(),
                newPost.isPublic(),
                newPost.isNoticePost(),
                newPost.getStatus(),
                newPost.getCreatedAt(),
                newPost.getModifiedAt(),
                newPost.getWriter(),
                parent != null ? parent.getPostNo() : null,
                null
                //null
        );
    }
}
