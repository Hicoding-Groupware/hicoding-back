package com.hook.hicodingapi.attendance.service;


import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.dto.response.DayAttendanceResponse;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceStudentService attendanceStudentService;
    private final EntityManager entityManager;

    /* 일일 출석표 조회 */
    @Transactional(readOnly = true)
    public List<DayAttendanceResponse> getAttendanceForDay(final Long cosCode, final LocalDate atdDate) {

        List<Attendance> attendanceList = attendanceRepository.findByCosCodeCosCodeAndAtdDate(cosCode, atdDate);

        List<DayAttendanceResponse> responseList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            responseList.add(DayAttendanceResponse.from(attendance));
        }

        return responseList;
    }

    /* 조회 시도시 일일 출석표 자동 생성 */
    @Transactional
    public void createNewAttendance(Long cosCode, LocalDate atdDate) {
        // 강의에 속한 모든 학생 조회
        List<Student> students = attendanceStudentService.getAllStudentByCourseCode(cosCode);
        // 출석 정보
        for (Student student : students) {
            Attendance newAttendance = new Attendance();
            // 엔티티 필드 초기화 등의 로직 수행
            attendanceRepository

        }
    }
}
