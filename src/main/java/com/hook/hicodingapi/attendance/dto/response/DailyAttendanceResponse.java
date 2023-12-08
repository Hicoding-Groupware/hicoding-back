package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public static DailyAttendanceResponse from(Student student, LocalDate atdDate) {
//        AttendanceStatusType status =
//                student.getAttendStdCode().isEmpty() ? null : student.getAttendStdCode().get(0).getAtdStatus();

        AttendanceStatusType status = student.getAttendStdCode().isEmpty() || !studentHasAttendanceOnDate(student, atdDate)
                ? null
                : student.getAttendStdCode().get(0).getAtdStatus();

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

    private static boolean studentHasAttendanceOnDate(Student student, LocalDate atdDate) {
        return student.getAttendStdCode().stream()
                .anyMatch(attendance -> atdDate.equals(attendance.getAtdDate()));
    }

}