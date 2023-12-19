package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.student.domain.Student;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 기존 등록
    boolean existsByStdCodeStdCodeAndCosCodeCosCodeAndAtdDate(Long stdCode, Long cosCode, LocalDate now);

    List<Attendance> findByAtdDateAndAtdStatusNot(LocalDate atdDate, AttendanceStatusType status);

    /* 월별 출석부 */
    List<Attendance>findByCosCodeCosCodeAndAtdDateBetween(Long cosCode, LocalDate startDate, LocalDate endDate);

}
