package com.hook.hicodingapi.course.domain.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.jwt.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyLectureRepository extends JpaRepository<Course, Long> {

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함 하여 조회 (강사) */
    @EntityGraph(attributePaths = {"teacher"})
    Page<Course> findByTeacherMemberNoAndCosSdtBeforeAndCosEdtAfter(Pageable pageable, Long memberNo, LocalDate cosSdt, LocalDate cosEdt);


    /* 2. 지난 강의 조회 - 페이징, 종강 일이 이미 지난 날짜일 경우 조회 (강사) */
    @EntityGraph(attributePaths = {"teacher"})
    Page<Course> findByTeacherMemberNoAndCosEdtBefore(Pageable pageable, Long memberNo, LocalDate cosEdt);


    /* 3. 예정 강의 조회 - 페이징, 개강 일이 아직 지나지 않은 날짜일 경우 조회 (강사) */
    @EntityGraph(attributePaths = {"teacher"})
    Page<Course> findByTeacherMemberNoAndCosSdtAfter(Pageable pageable, Long memberNo, LocalDate cosSdt);

}
