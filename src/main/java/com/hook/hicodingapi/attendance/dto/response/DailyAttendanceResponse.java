package com.hook.hicodingapi.attendance.dto.response;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;


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

        AttendanceStatusType status = null;

        // 첫 번째 등록된 수업만 고려합니다.
        Record firstRecord = student.getRecordList().stream().findFirst().orElse(null);

        if (firstRecord != null) {
            // 첫 번째 수업의 출석 목록에서 해당 날짜에 대한 출석 상태를 찾습니다.
            Optional<Attendance> attendanceOnDate = firstRecord.getCourse().getAttendCosCode().stream()
                    .filter(attendance -> Objects.equals(attendance.getAtdDate(), atdDate))
                    .findFirst();

            // 출석 상태가 존재하면 가져옵니다.
            status = attendanceOnDate.map(Attendance::getAtdStatus).orElse(null);
        }

        return new DailyAttendanceResponse(
                student.getRecordList().get(0).getCourse().getCosCode(),
                student.getRecordList().get(0).getCourse().getCosName(),
                student.getStdCode(),
                student.getStdName(),
                student.getStdBirth(),
                student.getStdPhone(),
                status
        );
    }
}


