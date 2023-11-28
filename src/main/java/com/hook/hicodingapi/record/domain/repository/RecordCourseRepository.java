package com.hook.hicodingapi.record.domain.repository;

import com.hook.hicodingapi.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordCourseRepository extends JpaRepository<Course, Long> {
}
