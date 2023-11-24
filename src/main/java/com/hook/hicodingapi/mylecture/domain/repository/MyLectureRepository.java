package com.hook.hicodingapi.mylecture.domain.repository;

import com.hook.hicodingapi.mylecture.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MyLectureRepository extends JpaRepository<Course, Long> {

    /* 1. 진행중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
    List<Course> findByCosSdtAndCosEdt(LocalDate cosSdt, LocalDate cosEdt);
}
