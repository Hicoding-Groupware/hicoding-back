package com.hook.hicodingapi.course.dto.resposne;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class DetailCourseLectureResponse {

    private final Long cosCode;
    private final String cosName;
    private final String teacherMemberName;
    private final String staffMemberName;
    private final String staffMemberEmail;
    private final DayStatusType dayStatus;
    private final TimeStatusType timeStatus;
    private final int curCnt;
    private final String roomName;
    private final String lecName;
    private final String textbook;
    private final String techStack;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;

    public static DetailCourseLectureResponse from(Course course) {
        return
                new DetailCourseLectureResponse(
                        course.getCosCode(),
                        course.getCosName(),
                        course.getTeacher().getMemberName(),
                        course.getStaff().getMemberName(),
                        course.getStaff().getMemberEmail(),
                        course.getDayStatus(),
                        course.getTimeStatus(),
                        course.getCurCnt(),
                        course.getClassroom().getRoomName(),
                        course.getLecCode().getLecName(),
                        course.getLecCode().getTextbook(),
                        course.getLecCode().getTechStack(),
                        course.getCosSdt(),
                        course.getCosEdt()
                );
    }
}
