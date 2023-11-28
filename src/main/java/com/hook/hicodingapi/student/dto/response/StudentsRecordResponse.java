package com.hook.hicodingapi.student.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
