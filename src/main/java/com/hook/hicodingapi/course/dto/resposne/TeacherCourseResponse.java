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
public class TeacherCourseResponse {

    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final String cosName;
    private final int curCnt;
    private final String teacherMemberName;
    private final DayStatusType dayStatus;
    private final TimeStatusType timeStatus;
    private final String roomName;

    public static TeacherCourseResponse from(Course course) {
        return
                new TeacherCourseResponse(
                course.getCosSdt(),
                course.getCosEdt(),
                course.getCosName(),
                course.getCurCnt(),
                course.getTeacher().getMemberName(),
                course.getDayStatus(),
                course.getTimeStatus(),
                course.getClassroom().getRoomName()
        );
    }
}
