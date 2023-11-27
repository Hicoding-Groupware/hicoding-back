package com.hook.hicodingapi.lecture.dto.response;

import com.hook.hicodingapi.lecture.domain.Lecture;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class TeacherLecturesResponse {

    private final Long lecCode;
    private final String lecName;
    private final String textbook;
    private final String techStack;

    public static TeacherLecturesResponse from(final Lecture lecture){
        return new TeacherLecturesResponse(
                lecture.getLecCode(),
                lecture.getLecName(),
                lecture.getTextbook(),
                lecture.getTechStack()
        );
    }
}
