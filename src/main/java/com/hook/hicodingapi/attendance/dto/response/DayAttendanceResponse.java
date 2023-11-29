package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class DayAttendanceResponse {


    private final Long stdCode;
    private final String stdName;
    private final Date stdBirth; // 형민 오빠랑 상의하기
    private final String stdPhone;
    private final AttendanceStatusType atdStatus;

    public static DayAttendanceResponse from(Attendance attendance) {
        return new DayAttendanceResponse(
                attendance.getStdCode().getStdCode(),
                attendance.getStdCode().getStdName(),
                attendance.getStdCode().getStdBirth(),
                attendance.getStdCode().getStdPhone(),
                attendance.getAtdStatus()
        );
    }
}
