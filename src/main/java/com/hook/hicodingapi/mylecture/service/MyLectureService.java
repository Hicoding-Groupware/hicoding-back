package com.hook.hicodingapi.mylecture.service;

import com.hook.hicodingapi.mylecture.domain.Course;
import com.hook.hicodingapi.mylecture.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.mylecture.dto.response.TeacherCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MyLectureService {

    private final MyLectureRepository myLectureRepository;

//    private Pageable getPageable(final Integer page) {
//        return PageRequest.of(page -1, 3, Sort.by("").descending());
//    }

    /* 1. 진행중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
    @Transactional(readOnly = true)
    public TeacherCourseResponse getTeacherCourseInProgress(LocalDate cosSdt, LocalDate cosEdt) {

        Course course = MyLectureRepository.findByCosSdtAndCosEdt(cosSdt, cosEdt)
                .orElseThrow(() -> new NotFoundException())

                return TeacherCourseResponse.from(course);
    }
}
