package com.hook.hicodingapi.attendance.service;

import com.hook.hicodingapi.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceStudentService {
    public List<Student> getAllStudentByCourseCode(Long cosCode) {

        return null;
    }
}
