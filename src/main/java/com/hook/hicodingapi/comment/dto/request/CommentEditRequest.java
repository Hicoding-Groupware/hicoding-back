package com.hook.hicodingapi.comment.dto.request;

import com.hook.hicodingapi.common.domain.type.StatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentEditRequest {
    private final String content;
    private final StatusType status;
}
