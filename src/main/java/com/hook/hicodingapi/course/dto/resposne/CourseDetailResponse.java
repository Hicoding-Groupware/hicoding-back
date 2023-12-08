package com.hook.hicodingapi.course.dto.resposne;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class CourseDetailResponse {

    private final Long cosCode;
    private final String cosName;
    private final Long lecCode;
    private final String textbook;
    private final String techStack;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final String roomCode;
    private final int capacity;
    private final int curCnt;
    private final String teacher;
    private final String staff;
    private final String staffPhone;
    private final String staffEmail;
    private final DayStatusType dayStatus;
    private final TimeStatusType timeStatus;
    private final String cosNotice;
    private final LocalDateTime modifiedAt;
    public static CourseDetailResponse from(final Course course) {
        return new CourseDetailResponse(
                course.getCosCode(),
                course.getCosName(),
                course.getLecCode().getLecCode(),
                course.getLecCode().getTextbook(),
                course.getLecCode().getTechStack(),
                course.getCosSdt(),
                course.getCosEdt(),
                course.getClassroom().getRoomName(),
                course.getCapacity(),
                course.getCurCnt(),
                course.getTeacher().getMemberName(),
                course.getStaff().getMemberName(),
                course.getStaff().getMemberPhone(),
                course.getStaff().getMemberEmail(),
                course.getDayStatus(),
                course.getTimeStatus(),
                course.getCosNotice(),
                course.getModifiedAt()
        );
    }



}
