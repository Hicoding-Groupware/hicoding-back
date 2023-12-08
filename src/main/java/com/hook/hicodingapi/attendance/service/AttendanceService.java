package com.hook.hicodingapi.attendance.service;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.common.exception.ConflictException;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;


@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    // 출석을 등록 하기 전에 이미 상태가 등록 되어 있는 출석이 있는지 확인
    @Transactional(readOnly = true)
    public void validateAttendanceCreate(AttendanceStatusType status, CustomUser customUser) {

        if(attendanceRepository.existsByAtdStatus(status)) {
            throw new ConflictException(ALREADY_CHECKED_IN);
        }
    }

    public Long save(AttendanceRegistRequest registAttendance, CustomUser customUser) {

        // 학생이 이 과정을 듣고 있는지 확인
        if (!studentRepository.existsByStdCodeAndRecordListCourseCosCode(
                registAttendance.getStdCode(), registAttendance.getCosCode())) {
            throw new NotFoundException(ENDROLLMENT_NOT_FOUND);
        }

        // 출석 등록을 시도하기 전에 이미 동일한, 학생, 강의, 날짜로 등록된 출석이 있는지 확인
        if(attendanceRepository.existsByStdCodeStdCodeAndCosCodeCosCodeAndAtdDate(
                registAttendance.getStdCode(), registAttendance.getCosCode(), LocalDate.now())) {
            throw new ConflictException(CONFLICT_ATTENDANCE_DATA);
        }

        Student student = studentRepository.findById(registAttendance.getStdCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STD_CODE));

        Course cosCode = courseRepository.findById(registAttendance.getCosCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COS_CODE));

        final Attendance newAttendance = Attendance.of(
                student,
                registAttendance.getStatus(),
                cosCode
        );

    final Attendance attendance = attendanceRepository.save(newAttendance);

    return attendance.getAtdCode();
    }


}