package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentsResponse {

    private final Long stdCode;
    private final String stdName;
    private final Date stdBirth;
    private final String stdMemo;
    private final String stdGender;
    private final String stdEmail;
    private final String stdPhone;
    private final LocalDateTime createdAt;

    public static StudentsResponse from(Student student) {
        return new StudentsResponse(
                student.getStdCode(),
                student.getStdName(),
                student.getStdBirth(),
                student.getStdMemo(),
                student.getStdGender(),
                student.getStdEmail(),
                student.getStdPhone(),
                student.getCreatedAt()
        );
    }
}
