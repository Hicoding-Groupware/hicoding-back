package com.hook.hicodingapi.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    final private String title;
    final private String content;
    final private boolean isPublic;
    final private boolean isNoticePost;
}
