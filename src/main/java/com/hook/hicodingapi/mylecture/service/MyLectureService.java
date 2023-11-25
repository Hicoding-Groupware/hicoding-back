package com.hook.hicodingapi.mylecture.service;

import com.hook.hicodingapi.mylecture.domain.Course;
import com.hook.hicodingapi.mylecture.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.mylecture.dto.response.TeacherCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional
public class MyLectureService {

    private final MyLectureRepository myLectureRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 3, Sort.by("cosEdt").descending());
    }

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함 하여 조회 (강사) */
//    @Transactional(readOnly = true)
//    public Page<TeacherCourseResponse> getTeacherCourses(final Integer page, final Long teacher, final LocalDate cosStd, final LocalDate cosEtd) {
//
//        Page<Course> courses = myLectureRepository
//                .findByTeacherAndCosSdtBeforeAndCosEdtAfter(
//                getPageable(page), teacher, cosStd, cosEtd);
//
//        return courses.map(course -> TeacherCourseResponse.from(course));
//
//
//    }

    /* 2. 지난 강의 조회 - 페이징, 종강일이 이미 지난 날짜일 경우 조회 (강사) */
    @Transactional(readOnly = true)
    public Page<TeacherCourseResponse> getTeacherCourse(final Integer page, final Long teacher, LocalDate cosEdt) {

        Page<Course> courses = myLectureRepository.findByTeacherAndCosEdtBefore(getPageable(page), teacher, cosEdt);

        return courses.map(course -> TeacherCourseResponse.from(course));
    }
}
