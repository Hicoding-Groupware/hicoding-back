package com.hook.hicodingapi.attendance.dto.request;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class AttendanceUpdateRequest {

    @NotNull
    private final Long atdCode;
    @NotNull
    private final LocalDate atdDate;
    @Min(value = 1)
    private final Long cosCode;
    @Min(value = 1)
    private final Long stdCode;
    @NotNull
    private final AttendanceStatusType status;


}
