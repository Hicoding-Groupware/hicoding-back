package com.hook.hicodingapi.course.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class CourseUpdateRequest {

    @NotBlank
    private final String lecName;
    @NotBlank
    private final String textbook;
    @NotBlank
    private final String techStack;

}


