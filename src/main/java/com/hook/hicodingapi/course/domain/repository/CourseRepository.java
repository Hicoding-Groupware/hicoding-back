package com.hook.hicodingapi.course.domain.repository;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import com.hook.hicodingapi.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByStatusNotAndCosSdtLessThanEqualAndCosEdtGreaterThanEqual(Pageable pageable, CourseStatusType courseStatusType, LocalDate sdt, LocalDate edt);
    Page<Course> findByStatusNotAndCosSdtGreaterThan(Pageable pageable, CourseStatusType courseStatusType, LocalDate sdt);

    Optional<Course> findByCosCodeAndStatusNot(Long cosCode, CourseStatusType courseStatusType);

    @EntityGraph(attributePaths = {"teacher"})
    Page<Course> findByStatus(Pageable pageable, CourseStatusType courseStatusType);

    Page<Course> findByCosNameContainsAndStatusAndCosEdtAfter(Pageable coursePageable, String cosName, CourseStatusType courseStatusType, LocalDate currentDay);

    Optional<Course> findByCosCodeAndStatus(Long cosCode, CourseStatusType courseStatusType);

}
