package com.hook.hicodingapi.attendance.service;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.course.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MyLectureRepository myLectureRepository;
    private final StudentRepository studentRepository;

//    public Long save(AttendanceRegistRequest registAttendance) {
//
//        Attendance attendance = attendanceRepository.findById(registAttendance.get)
//
//    }
//

}