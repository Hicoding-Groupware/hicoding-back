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

    private final Long cosCode;
    private final LocalDate cosSdt;
    private final LocalDate cosEdt;
    private final String cosName;
    private final Long curCnt;
//    private final Long teacher;
    private final String memberName;
    private final DayStatusType dayStatus;
    private final TimeStatusType timeStatus;
//    private final Long roomCode;
    private final String roomName;

    public static TeacherCourseResponse from(Course course) {
        return
                new TeacherCourseResponse(
                course.getCosCode(),
                course.getCosSdt(),
                course.getCosEdt(),
                course.getCosName(),
                course.getCurCnt(),
//              course.getMember().getMemberNo(),
//              course.getMember().getMemberName(),
//                course.getTeacher().getMemberNo(),
                course.getTeacher().getMemberName(),
                course.getDayStatus(),
                course.getTimeStatus(),
//                course.getClassroom().getRoomCode(),
                course.getClassroom().getRoomName()
        );
    }
}
