package com.hook.hicodingapi.course.dto.request;

import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class CourseUpdateRequest {

    @NotBlank
    private final String cosName;
    @Min(value = 1)
    private final Long lecCode;
    @Min(value = 1)
    private final Long teacher;
    @Min(value = 1)
    private final Long staff;
    @Min(value = 1)
    private final Long roomCode;
    @NotNull
    private final LocalDate cosSdt;
    @NotNull
    private final LocalDate cosEdt;
    @Min(value = 1)
    private final int capacity;
    @Min(value = 0)
    private final int curCnt;
    @NotNull
    private final DayStatusType dayStatus;
    @NotNull
    private final TimeStatusType timeStatus;
    private final String cosNotice;

}


