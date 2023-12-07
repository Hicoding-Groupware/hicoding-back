package com.hook.hicodingapi.student.dto.response;

import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentDetailResponse {

    private final Long stdCode;
    private final String stdName;
    private final String stdGender;
    private final LocalDate stdBirth;
    private final String stdPhone;
    private final String stdEmail;
    private final String postNo;
    private final String address;
    private final String detailAddress;
    private final String stdMemo;

    public static StudentDetailResponse from(Student student) {
        return new StudentDetailResponse(
                student.getStdCode(),
                student.getStdName(),
                student.getStdGender(),
                student.getStdBirth(),
                student.getStdPhone(),
                student.getStdEmail(),
                student.getPostNo(),
                student.getAddress(),
                student.getDetailAddress(),
                student.getStdMemo()
        );
    }
}
