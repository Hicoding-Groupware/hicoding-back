package com.hook.hicodingapi.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    NOT_FOUND_LEC_CODE(4000,"강의코드에 해당하는 강의가 존재하지 않습니다."),

    NOT_FOUND_COS_CODE(4001,"과정코드에 해당하는 과정이 존재하지 않습니다."),

    NOT_FOUND_MEMBER_CODE(4002,"직원코드에 해당하는 직원이 존재하지 않습니다."),

    NOT_FOUND_ROOM_CODE(4003,"강의실코드에 해당하는 강의실이 존재하지 않습니다."),

    NOT_FOUND_STD_CODE(5000, "학생코드에 해당하는 해당 학생이 존재하지 않습니다."),

    NOT_FOUND_COURSE_CODE(5001, "수강코드에 해당하는 해당 강좌가 존재하지 않습니다."),

    NOT_FOUND_RECORD_CODE(5002, "수강이력이 존재하지 않습니다."),

    NOT_ENOUGH_CAPACITY(5003, "해당과정이 꽉찼습니다.");



    private final int code;
    private final String message;


}
