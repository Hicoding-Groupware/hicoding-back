package com.hook.hicodingapi.comment.dto.request;

import com.hook.hicodingapi.board.domain.type.BoardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentCreationRequest {

    private final String content;
    private final Long postNo;
    private final Long writerNo;
    private final Long parentNo;
}
