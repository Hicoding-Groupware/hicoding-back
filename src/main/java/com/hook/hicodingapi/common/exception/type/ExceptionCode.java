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

    NOT_ENOUGH_CAPACITY(5003, "해당과정이 꽉찼습니다."),

    /*------------ 로그인 exceptionCode --------------*/

    FAIL_LOGIN(4000, "로그인에 실패하였습니다"),

    UNAUTHORIZED(4001, "인증되지 않은 요청입니다."),

    NOT_FOUND_MEMBER_ID(4002, "아이디에 해당하는 유저가 없습니다."),

    ACCESS_DENIED(4003, "허가 되지 않은 요청입니다."),

    /*------------ 파일 exceptionCode --------------*/
    FAIL_TO_UPLOAD_FILE(1001, "파일 저장에 실패하였습니다."),

    FAIL_TO_DELETE_FILE(1002, "파일 삭제에 실패하였습니다.");


    private final int code;
    private final String message;


}
