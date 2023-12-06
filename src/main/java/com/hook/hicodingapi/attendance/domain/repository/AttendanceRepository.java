package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
