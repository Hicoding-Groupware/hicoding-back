package com.hook.hicodingapi.student.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@RequiredArgsConstructor
@Getter
public class StudentRegistRequest {

    @NotBlank
    private final String stdName;

    @NotBlank
    private final String stdGender;

    @NotNull
    private final LocalDate stdBirth;

    @NotBlank
    private final String stdPhone;

    private final String stdEmail;

    private final String postNo;

    private final String address;

    private final String detailAddress;

    private final String stdMemo;

}
