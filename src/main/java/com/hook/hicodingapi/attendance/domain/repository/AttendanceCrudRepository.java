package com.hook.hicodingapi.attendance.domain.repository;

import com.hook.hicodingapi.attendance.domain.Attendance;
import org.springframework.data.repository.CrudRepository;


public interface AttendanceCrudRepository extends CrudRepository<Attendance, Long> {
}
