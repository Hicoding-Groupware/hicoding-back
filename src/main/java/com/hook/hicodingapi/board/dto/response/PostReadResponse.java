package com.hook.hicodingapi.board.dto.response;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostReadResponse {
    private Long no;
    private String title;
    private String content;
    private int views;
    private int likesCnt;
    private boolean isPublic;
    private boolean isNoticePost;
    private Member writer;
    private List<Comment> commentList;

    public static PostReadResponse from(final Post newPost) {
        return new PostReadResponse(
                newPost.getPostNo(),
                newPost.getPostTitle(),
                newPost.getPostContent(),
                newPost.getViews(),
                newPost.getLikesCount(),
                newPost.isPublic(),
                newPost.isNoticePost(),
                newPost.getWriter(),
                newPost.getCommentList()
        );
    }
}
