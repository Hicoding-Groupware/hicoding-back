package com.hook.hicodingapi.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.board.domain.Post;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
public class LikesPostResponse {

    private final Integer likesCount;
    private final Long postNo;
    private final Long memberNo;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private LikesPostResponse(Integer likesCount, Long postNo, Long memberNo) {
        this.likesCount = likesCount;
        this.postNo = postNo;
        this.memberNo = memberNo;
    }

    public static LikesPostResponse from(final Post post) {
        return new LikesPostResponse(
            post.getLikesCount(),
            post.getPostNo(),
            post.getWriter().getMemberNo()
        );
    }
}
