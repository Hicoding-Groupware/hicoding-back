package com.hook.hicodingapi.lecture.dto.request;

import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class LectureUpdateRequest {

    @NotBlank
    private final String lecName;
    @NotBlank
    private final String textbook;
    @NotBlank
    private final String techStack;

}


