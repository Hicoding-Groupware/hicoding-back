package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStdCodeStdCodeAndCosCodeCosCodeAndAtdDate(Long stdCode, Long cosCode, LocalDate now);
    boolean existsByAtdStatus(AttendanceStatusType status);
}
