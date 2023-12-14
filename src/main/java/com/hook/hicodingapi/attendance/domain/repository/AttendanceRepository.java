package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 기존 등록
    boolean existsByStdCodeStdCodeAndCosCodeCosCodeAndAtdDate(Long stdCode, Long cosCode, LocalDate now);

    // 임시 등록 + 날짜 상관없이 등록 가능
//    boolean existsByStdCodeStdCodeAndCosCodeCosCode(Long stdCode, Long cosCode);
    List<Attendance> findByAtdDateAndAtdStatusNot(LocalDate atdDate, AttendanceStatusType status);
}
