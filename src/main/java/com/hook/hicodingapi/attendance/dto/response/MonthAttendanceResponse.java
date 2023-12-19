package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MonthAttendanceResponse {

    private final Long cosCode;
    private final String cosName;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final Long stdCode;
    private final String stdName;
    private final Long atdCode;
    private final LocalDate atdDate;
    private final DayStatusType dayStatus;
    private final AttendanceStatusType AttendanceStatus;


    public static MonthAttendanceResponse from(Attendance attendance) {

        return new MonthAttendanceResponse(
                attendance.getStdCode().getRecordList().get(0).getCourse().getCosCode(),
                attendance.getStdCode().getRecordList().get(0).getCourse().getCosName(),
                attendance.getStdCode().getRecordList().get(0).getCourse().getCosSdt(),
                attendance.getStdCode().getRecordList().get(0).getCourse().getCosEdt(),
                attendance.getStdCode().getStdCode(),
                attendance.getStdCode().getStdName(),
                attendance.getAtdCode(),
                attendance.getAtdDate(),
                attendance.getCosCode().getDayStatus(),
                attendance.getAtdStatus()
        );
    }

}

