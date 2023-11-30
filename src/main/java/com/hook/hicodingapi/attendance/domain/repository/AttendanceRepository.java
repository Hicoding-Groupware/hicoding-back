package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

//    List<Attendance> findByCosCodeCosCodeAndAtdDate(Long cosCode, LocalDate atdDate);



}
