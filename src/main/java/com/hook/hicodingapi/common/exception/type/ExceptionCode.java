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

    INVALID_DATE_FOR_COURSE(5004, "날짜가 코스 유형과 일치하지 않습니다."),

    /*------------ 로그인 exceptionCode --------------*/

    FAIL_LOGIN(4000, "로그인에 실패하였습니다"),

    UNAUTHORIZED(4001, "인증되지 않은 요청입니다."),

    NOT_FOUND_MEMBER_ID(4002, "아이디에 해당하는 유저가 없습니다."),

    ACCESS_DENIED(4003, "허가 되지 않은 요청입니다."),

    // 회원
    NOT_FOUND_MEMBER_ROLE_CODE(5002, "회원 권한이 일치하지 않습니다."),

    /*------------ 파일 및 사진 exceptionCode --------------*/
    FAIL_TO_UPLOAD_FILE(1001, "파일 저장에 실패하였습니다."),

    FAIL_TO_DELETE_FILE(1002, "파일 삭제에 실패하였습니다."),
    /*----------- 출석 체크 exceptionCode -------------*/

    NOT_ALLOWED_EARLY_ATTENDANCE(6000, "출결 여부를 미리 등록 할 수 없습니다."),

    CONFLICT_ATTENDANCE_DATA(6001, "이미 출석이 완료 되었습니다."),

    ENDROLLMENT_NOT_FOUND(6002, "이 과정을 듣지 않는 학생입니다."),

    NOT_FOUND_ATD_CODE(6003, "이 출석코드는 수정할 수 없습니다."),

    NOT_FOUND_FILE_NO(1003, "파일을 찾을 수 없습니다."),


    /* 게시판 */
    NOT_FOUND_POST_CODE(1000, "게시글을 가져올 수 없습니다."),
    NOT_FOUND_POSTS_CODE(1001, "게시글들을 가져올 수 없습니다."),
    NOT_FOUND_BOARD_TYPE_CODE(1002, "해당 게시판 타입과 일치하지 않습니다."),
    NOT_FOUND_WRITER_CODE(1003, "게시글 작성자가 존재하지 않습니다."),
    NOT_FOUND_READ_MEMBER_CODE(1004, "조회한 멤버를 가져올 수 없습니다"),
    FAIL_CREATION_POST_CODE(1005, "게시글을 생성할 수 없습니다."),
    FAIL_CONVERT_HIERARCHICAL_CODE(1006, "게시글 목록 구조 분해를 할 수 없습니다."),

    // 댓글
    NOT_FOUND_COMMENT_CODE(2000, "댓글을 가져올 수 없습니다."),
    NOT_FOUND_COMMENTS_CODE(2001, "댓글들을 가져올 수 없습니다."),
    FAIL_CREATION_COMMENT_CODE(2004, "댓글을 생성할 수 없습니다."),
    CONFLICT_PARENT_AND_CHILD_COMMENT_CODE(2005, "부모 댓글과 자식 댓글의 게시글 번호가 일치하지 않습니다."),

    // 게시판 기록
    NOT_FOUND_BOARD_RECORD_CODE(3000, "게시판 기록을 가져올 수 없습니다."),
    NOT_FOUND_BOARD_RECORDS_CODE(3001, "게시판 기록을 가져올 수 없습니다."),
    NOT_FOUND_BOARD_RECORD_TYPE_CODE(3002, "해당 게시판 기록 타입과 일치하지 않습니다.");

    private final int code;
    private final String message;


}
