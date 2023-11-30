package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.hook.hicodingapi.attendance.dto.response.StudentAttendance;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class StudentAttendanceResponse {

    //    private final String cosName;
    private final Long stdCode;
    private final String stdName;
    private final LocalDate stdBirth;
    private final String stdPhone;
    private final AttendanceStatusType attendanceStatus;

    /* Student에서 Attendance로 연결 */
    public static StudentAttendanceResponse from(Student student) {

        List<StudentAttendance> attendances = student.getAStdCode()
                .stream().map(attendance -> StudentAttendance.from(attendance))
                .collect(Collectors.toList());

        AttendanceStatusType attendanceStatus = null;

        for (AttendanceStatusType status : AttendanceStatusType.values()) {
            if (attendances.stream().anyMatch(a -> a.getAtdStatus() == status)) {
                attendanceStatus = status;
                break; // 찾았으면 반복문 종료
            }
        }

        return
                new StudentAttendanceResponse(
                        student.getStdCode(),
                        student.getStdName(),
                        student.getStdBirth(),
                        student.getStdPhone(),
                        attendanceStatus// 출결관리랑 연결
                );
    }
}
