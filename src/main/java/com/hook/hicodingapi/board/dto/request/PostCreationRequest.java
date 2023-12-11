package com.hook.hicodingapi.board.dto.request;

import com.hook.hicodingapi.board.domain.type.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@RequiredArgsConstructor
@Getter
public class PostCreationRequest {
    final private String title;
    final private String content;
    final private boolean isPublic;
    final private boolean isNoticePost;
    final private Long writerNo;
    final private Long parentNo;
}
