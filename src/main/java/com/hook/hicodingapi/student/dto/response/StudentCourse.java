package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.record.domain.Record;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentCourse {
    private final String cosName;
    //private final String teacher;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;

    public static StudentCourse from(Record record) {
        return new StudentCourse(
                record.getCourse().getCosName(),
                //record.getCourse().getTeacher().getMemberName(),
                record.getCourse().getCosSdt(),
                record.getCourse().getCosEdt()
        );
    }
}
