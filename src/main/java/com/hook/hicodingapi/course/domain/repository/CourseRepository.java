package com.hook.hicodingapi.course.domain.repository;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatus(Pageable pageable, CourseStatusType courseStatusType);
}
