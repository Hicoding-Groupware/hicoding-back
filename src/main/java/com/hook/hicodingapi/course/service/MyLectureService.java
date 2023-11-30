package com.hook.hicodingapi.course.service;

import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.course.dto.resposne.DetailCourseLectureResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_COURSE_CODE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_LEC_CODE;


@Service
@RequiredArgsConstructor
@Transactional
public class MyLectureService {

    private final MyLectureRepository myLectureRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 3, Sort.by("cosEdt").descending());
    }

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함 하여 조회 (강사) */
    @Transactional(readOnly = true)
    public Page<TeacherCourseResponse> getTeacherCourseCosSdtAndCosEdt(final Integer page, final Long memberNo, LocalDate cosStd, LocalDate cosEtd) {


        Page<Course> courses = myLectureRepository
                .findByTeacherMemberNoAndCosSdtBeforeAndCosEdtAfter(
                getPageable(page), memberNo, cosStd.minusDays(1), cosEtd.plusDays(1));

        return courses.map(course -> TeacherCourseResponse.from(course));
    }


    /* 2. 지난 강의 조회 - 페이징, 종강 일이 이미 지난 날짜일 경우 조회 (강사) */
    @Transactional(readOnly = true)
    public Page<TeacherCourseResponse> getTeacherCourseCosEdt(final Integer page, final Long memberNo, LocalDate cosEdt) {

        Page<Course> courses = myLectureRepository.findByTeacherMemberNoAndCosEdtBefore(getPageable(page), memberNo, cosEdt);

        return courses.map(course -> TeacherCourseResponse.from(course));
    }


    /* 3. 예정 강의 조회 - 페이징, 개강 일이 아직 지나지 않은 날짜일 경우 조회 (강사) */
    @Transactional(readOnly = true)
    public Page<TeacherCourseResponse> getTeacherCourseCosSdt(final Integer page, final Long memberNo, LocalDate cosSdt) {

        Page<Course> courses = myLectureRepository.findByTeacherMemberNoAndCosSdtAfter(getPageable(page), memberNo, cosSdt);

        return courses.map(course -> TeacherCourseResponse.from(course));
    }


    /* 4. 강의 상세 조회 - 조건 없이 해당 강의 상세 목록 모두 조회 가능 (강사) */
    @Transactional(readOnly = true)
    public DetailCourseLectureResponse getTeacherCosCode(final Long cosCode) {

        Course course = myLectureRepository.findByCosCode(cosCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COURSE_CODE));

        return DetailCourseLectureResponse.from(course);
    }
}
