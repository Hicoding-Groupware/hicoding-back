package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentCourseResponse {

    private final Long cosCode;
    private final String cosName;
    private final String teacher;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;

    public static StudentCourseResponse from(Course course) {
        return new StudentCourseResponse(
                course.getCosCode(),
                course.getCosName(),
                course.getTeacher().getMemberName(),
                course.getCosSdt(),
                course.getCosEdt()

        );

    }


}
