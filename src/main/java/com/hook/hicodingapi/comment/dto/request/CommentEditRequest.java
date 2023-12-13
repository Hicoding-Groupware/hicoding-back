package com.hook.hicodingapi.comment.dto.request;

import com.hook.hicodingapi.common.domain.type.StatusType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CommentEditRequest {
    private final String content;
    private final StatusType status;
}
