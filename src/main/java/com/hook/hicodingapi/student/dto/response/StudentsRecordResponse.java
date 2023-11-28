package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentsRecordResponse {

    private final String stdName;
    private final Date stdBirth;
    private final String cosName;
    private final String memberName;
    private final Date cosSdt;
    private final Date cosEdt;
    private final String stdPhone;
    private final Date registedDate;



}
