package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentAttendance {

    private final Long atdCode;
    private final LocalDate atdDate;
    private final AttendanceStatusType atdStatus;

    public static StudentAttendance from(Attendance attendance) {

        return new StudentAttendance(
                attendance.getAtdCode(),
                attendance.getAtdDate(),
                attendance.getAtdStatus()
        );
    }

}


// 학생 정보와 출석 상태 조회 로직까지는 얼추..? 이제 코스랑 연결해야함