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

    private final Long cosCode;
    private final String cosName;
    private final Long stdCode;
    private final String stdName;
    private final LocalDate stdBirth;
    private final String stdPhone;
    private final AttendanceStatusType attendanceStatus;

    public static DailyAttendanceResponse from(Student student, LocalDate atdDate) {

        AttendanceStatusType status = student.getAttendStdCode().isEmpty() || !studentHasAttendanceOnDate(student, atdDate)
                ? null
                : student.getAttendStdCode().get(0).getAtdStatus();
        // 학생의 attendStdCode 목록이 비어 있으면(즉, 출석 기록이 없으면),
        // 또는 학생이 지정한 날짜(atdDate)에 출석 기록이 없으면,
        // status에 null을 할당한다.
        // 그렇지 않으면, student.getAttendStdCode().get(0).getAtdStatus()를 통해 첫 번째 출석 기록의 상태를 가져와서
        // status에 할당한다.

        return
                new DailyAttendanceResponse(
                        student.getRecordList().get(0).getCourse().getCosCode(),
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