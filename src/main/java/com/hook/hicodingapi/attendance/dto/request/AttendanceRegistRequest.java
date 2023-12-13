package com.hook.hicodingapi.attendance.dto.request;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class AttendanceRegistRequest {

    @NotNull
    private final AttendanceStatusType status;
    @Min(value = 1)
    private final Long stdCode;
    @Min(value = 1)
    private final Long cosCode;
}
