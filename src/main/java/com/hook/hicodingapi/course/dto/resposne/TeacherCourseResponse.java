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
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final LocalTime claSt;
    private final LocalTime clsEt;
    private final String cosName;
    private final Long curCnt;
    private final Long teacher;
//    private final Long memberNo;
    private final String memberName;
    private final String dayStatus;



    public static TeacherCourseResponse from(Course course) {
        return new TeacherCourseResponse(
                course.getCosCode(),
                course.getCosSdt(),
                course.getCosEdt(),
                course.getClaSt(),
                course.getClaEt(),
                course.getCosName(),
                course.getCurCnt(),
                course.getMember().getMemberNo(),
                course.getMember().getMemberName(),
                course.getDayStatus()
        );
    }
}
