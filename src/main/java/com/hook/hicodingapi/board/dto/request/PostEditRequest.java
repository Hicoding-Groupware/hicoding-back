package com.hook.hicodingapi.board.dto.request;

import com.hook.hicodingapi.common.domain.type.StatusType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostEditRequest {
    private final String title;
    private final String content;
    private final boolean isPublic;
    private final boolean isNoticePost;
    private final StatusType status;
}
