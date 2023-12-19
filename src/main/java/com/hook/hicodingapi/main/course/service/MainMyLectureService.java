package com.hook.hicodingapi.main.course.service;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.course.dto.resposne.DetailCourseLectureResponse;
import com.hook.hicodingapi.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MainMyLectureService {

    private final MyLectureRepository myLectureRepository;


    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 2, Sort.by("cosEdt").descending());
    }


    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함 하여 조회 (강사) */
    @Transactional(readOnly = true)
    public Page<DetailCourseLectureResponse> getTeacherCourseCosSdtAndCosEdt(Integer page, CustomUser customUser, LocalDate cosStd, LocalDate cosEtd) {


        Page<Course> courses = myLectureRepository
                .findByTeacherMemberNoAndCosSdtBeforeAndCosEdtAfter(
                        getPageable(page), customUser.getMemberNo(), cosStd.minusDays(1), cosEtd.plusDays(1));

        return courses.map(course -> DetailCourseLectureResponse.from(course));
    }
}
