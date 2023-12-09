package com.hook.hicodingapi.board.dto.response;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                newPost.getWriter(),
                newPost.getParent().getPostNo()
        );
    }
}
