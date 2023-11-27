package com.hook.hicodingapi.course.dto.resposne;


import com.hook.hicodingapi.course.domain.Course;
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
    private final LocalTime claSt;
    private final LocalTime clsEt;
    private final String dayStatus;
    private final Long curCnt;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;

    public static TeacherCourseResponse from(Course course) {
        return new TeacherCourseResponse(
                course.getCosCode(),
                course.getCosName(),
                course.getTeacher(),
                course.getMember().getMemberName(),
                course.getClaSt(),
                course.getClaEt(),
                course.getDayStatus(),
                course.getCurCnt(),
                course.getCosSdt(),
                course.getCosEdt()
        );
    }
}
