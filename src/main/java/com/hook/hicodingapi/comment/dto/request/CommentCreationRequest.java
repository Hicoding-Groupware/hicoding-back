package com.hook.hicodingapi.comment.dto.request;

import com.hook.hicodingapi.board.domain.type.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentCreationRequest {

    private final String content;
    private final Long postNo;
    private final Long writerNo;
    private final Long parentNo;
}
