package com.hook.hicodingapi.attendance.service;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.attendance.dto.request.AttendanceUpdateRequest;
import com.hook.hicodingapi.common.exception.ConflictException;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType.ATTENDANCE;
import static com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType.SICK_LEAVE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;


@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    /* 5. 출석 등록 */
    public List<Long> save(List<AttendanceRegistRequest> registAttendances) {
        List<Long> savedAttendanceIds = new ArrayList<>();

        for (AttendanceRegistRequest registAttendance : registAttendances) {
            validateAttendanceRequest(registAttendance); // 출석 등록 유효성 검증

            Student student = getStudent(registAttendance.getStdCode());
            Course cosCode = getCourse(registAttendance.getCosCode());

            Attendance newAttendance = Attendance.of( // final?
                    student,
                    registAttendance.getStatus(),
                    cosCode
            );

            Attendance savedAttendance = attendanceRepository.save(newAttendance);
            savedAttendanceIds.add(savedAttendance.getAtdCode());
        }
        return savedAttendanceIds;
    }

    private void validateAttendanceRequest(AttendanceRegistRequest registAttendance) {
        // 학생이 이 과정을 듣고 있는지 확인
        if (!studentRepository.existsByStdCodeAndRecordListCourseCosCode(
                registAttendance.getStdCode(), registAttendance.getCosCode())) {
            throw new NotFoundException(ENDROLLMENT_NOT_FOUND);
        }

        // 출석 등록을 시도 하기 전에 오늘 이미 동일한, 학생, 강의, 날짜로 등록된 출석이 있는지 확인
        // 기존 등록
        if (attendanceRepository.existsByStdCodeStdCodeAndCosCodeCosCodeAndAtdDate(
                registAttendance.getStdCode(), registAttendance.getCosCode(), LocalDate.now())) {
            throw new ConflictException(CONFLICT_ATTENDANCE_DATA);
        }

    }

    private Student getStudent(Long stdCode) {
        return studentRepository.findById(stdCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STD_CODE));
    }

    private Course getCourse(Long cosCode) {
        return courseRepository.findById(cosCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COS_CODE));
    }




    /* 6. 출석 수정 */
    public List<Attendance> update(LocalDate atdDate, List<AttendanceUpdateRequest> attendanceRequests) {
        // 기존 출석 정보 조회
        List<Attendance> existingAttendances = attendanceRepository.findByAtdDateAndAtdStatusNot(atdDate, SICK_LEAVE);

        // 비즈니스 로직: 기존 출석 정보 업데이트
        for (Attendance existingAttendance : existingAttendances) {
            for (AttendanceUpdateRequest request : attendanceRequests) {
                // 출석 정보의 일련번호(ID)를 비교하여 일치하는 경우에 업데이트 수행
                if (existingAttendance.getAtdCode().equals(request.getAtdCode())) {

                    Course course = courseRepository.findById(request.getCosCode())
                            .orElseThrow(() -> new NotFoundException(NOT_FOUND_COS_CODE));

                    Student student = studentRepository.findById(request.getStdCode())
                            .orElseThrow(() -> new NotFoundException(NOT_FOUND_STD_CODE));

                    existingAttendance.update(
                            request.getAtdDate(),
                            course,
                            student,
                            request.getAtdCode(),
                            request.getStatus()
                    );
                }
            }
        }
        // 업데이트된 출석 정보를 저장
//        attendanceRepository.saveAll(existingAttendances);
        return existingAttendances;

    }
}
