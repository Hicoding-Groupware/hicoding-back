package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentsRecordResponse {

    private final Long stdCode;
    private final String stdName;
    private final List<StudentCourse> courseList;
    private final String stdPhone;



    public static StudentsRecordResponse from(Student student) {

        List<StudentCourse> courseList = student.getRecordList()
                .stream().map(record -> StudentCourse.from(record))
                .collect(Collectors.toList());

        return new StudentsRecordResponse(
                student.getStdCode(),
                student.getStdName(),
                courseList,
                student.getStdPhone()

        );
    }
}
