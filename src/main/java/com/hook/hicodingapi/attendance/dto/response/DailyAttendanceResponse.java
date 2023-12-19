package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.Comparator;

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
    private final LocalDate atdDate;
    private final Long atdCode;
    private final AttendanceStatusType attendanceStatus;

    public static DailyAttendanceResponse from(Student student, LocalDate atdDate) {

        AttendanceStatusType status = getAttendanceStatusOnDate(student, atdDate);

        Long atdCode = student.getAttendStdCode()
                .stream()
                .filter(attendance -> atdDate.equals(attendance.getAtdDate()))
                .max(Comparator.comparing(Attendance::getAtdDate))
                .map(Attendance::getAtdCode)
                .orElse(null);

        return new DailyAttendanceResponse(
                student.getRecordList().get(0).getCourse().getCosCode(),
                student.getRecordList().get(0).getCourse().getCosName(),
                student.getStdCode(),
                student.getStdName(),
                student.getStdBirth(),
                student.getStdPhone(),
                atdDate,
                atdCode,
                status
        );
    }

    private static AttendanceStatusType getAttendanceStatusOnDate(Student student, LocalDate atdDate) {
        return student.getAttendStdCode().stream()
                .filter(attendance -> atdDate.equals(attendance.getAtdDate()))
                .findFirst()
                .map(attendance -> attendance.getAtdStatus())  // 여기서 직접 값을 추출
                .orElse(null);
    }
}
