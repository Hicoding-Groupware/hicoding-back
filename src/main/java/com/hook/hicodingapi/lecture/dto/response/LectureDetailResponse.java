package com.hook.hicodingapi.lecture.dto.response;

import com.hook.hicodingapi.lecture.domain.Lecture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class LectureDetailResponse {

    private final Long lecCode;
    private final String lecName;
    private final String textbook;
    private final String techStack;

    public static LectureDetailResponse from(Lecture lecture) {
        return new LectureDetailResponse(
                lecture.getLecCode(),
                lecture.getLecName(),
                lecture.getTextbook(),
                lecture.getTechStack()
        );
    }
}
