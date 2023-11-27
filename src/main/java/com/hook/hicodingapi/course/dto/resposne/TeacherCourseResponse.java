package com.hook.hicodingapi.course.dto.resposne;


import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class TeacherCourseResponse {

    private final Long cosCode;
    private final String cosName;
    private final Long teacher;
    private final String memberName;
    private final DayStatusType dayStatus;
    private final Long curCnt;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;

    public static TeacherCourseResponse from(Course course) {
        return new TeacherCourseResponse(
                course.getCosCode(),
                course.getCosName(),
                course.getMember().getMemberNo(),
                course.getMember().getMemberName(),
                course.getDayStatus(),
                course.getCurCnt(),
                course.getCosSdt(),
                course.getCosEdt()
        );
    }
}
