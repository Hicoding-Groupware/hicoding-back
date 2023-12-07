package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class DailyAttendanceResponse {

    private final String cosName;
    private final Long stdCode;
    private final String stdName;
    private final LocalDate stdBirth;
    private final String stdPhone;
    private final AttendanceStatusType attendanceStatus;

    public static DailyAttendanceResponse from(Student student) {
        AttendanceStatusType status =
                student.getAttendStdCode().isEmpty() ? null : student.getAttendStdCode().get(0).getAtdStatus();

        return
                new DailyAttendanceResponse(
                        student.getRecordList().get(0).getCourse().getCosName(),
                        student.getStdCode(),
                        student.getStdName(),
                        student.getStdBirth(),
                        student.getStdPhone(),
                        status
                );
    }
}
