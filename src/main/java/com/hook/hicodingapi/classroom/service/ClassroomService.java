package com.hook.hicodingapi.classroom.service;

import com.hook.hicodingapi.classroom.domain.Classroom;
import com.hook.hicodingapi.classroom.domain.repository.ClassroomRepository;
import com.hook.hicodingapi.classroom.dto.response.ClassroomResponse;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public List<Classroom> getClassrooms() {
        return classroomRepository.findAll();
    }
}
