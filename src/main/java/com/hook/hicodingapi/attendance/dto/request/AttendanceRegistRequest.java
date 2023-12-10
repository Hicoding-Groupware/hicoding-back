package com.hook.hicodingapi.attendance.dto.request;

import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class AttendanceRegistRequest {

    // Bean Validation API에서 제공하는 어노테이션 중 하나.
    // 숫자 형식이 특정 최솟값 이상이어야 한다는 제약 설정.
    // 해당 필드는 반드시 1이상의 값이어야 한다.
    @NotNull
    private final AttendanceStatusType status;
    @Min(value = 1)
    private final Long stdCode;
    @Min(value = 1)
    private final Long cosCode;



}
