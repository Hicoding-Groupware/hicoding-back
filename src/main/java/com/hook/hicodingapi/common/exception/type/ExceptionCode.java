package com.hook.hicodingapi.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    NOT_FOUND_STD_CODE(5000, "학생코드에 해당하는 해당 학생이 존재하지 않습니다."),
    NOT_FOUND_LEC_CODE(4000,"강의코드에 해당하는 강의가 존재하지 않습니다."),
    NOT_FOUND_COURSE_CODE(6000, "과정코드에 해당하는 과정이 존재하지 않습니다.");

    private final int code;
    private final String message;


}
