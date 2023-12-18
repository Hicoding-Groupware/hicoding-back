package com.hook.hicodingapi.course.dto.resposne;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class CourseResponse {

    private final Long cosCode;
    private final String cosName;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;



}
