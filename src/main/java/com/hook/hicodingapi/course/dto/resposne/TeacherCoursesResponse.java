package com.hook.hicodingapi.course.dto.resposne;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class TeacherCoursesResponse {
    private final Long cosCode;
    private final String cosName;
    private final String lecCode;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final String roomCode;
    private final int capacity;
    private final int curCnt;
    private final String teacher;
    private final String staff;
    private final DayStatusType dayStatus;
    private final TimeStatusType timeStatus;
    public static TeacherCoursesResponse from(final Course course) {
        return new TeacherCoursesResponse(
                course.getCosCode(),
                course.getCosName(),
                course.getLecCode().getLecName(),
                course.getCosSdt(),
                course.getCosEdt(),
                course.getClassroom().getRoomName(),
                course.getCapacity(),
                course.getCurCnt(),
                course.getTeacher().getMemberName(),
                course.getStaff().getMemberName(),
                course.getDayStatus(),
                course.getTimeStatus()
        );
    }
}
