package com.hook.hicodingapi.mylecture.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.mylecture.dto.resposne.TeacherCourseResponse;
import com.hook.hicodingapi.mylecture.service.MyLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my_lecture")
public class MyLectureController {

    private final MyLectureService myLectureService;

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
//    @GetMapping("/in-progress")
//    public ResponseEntity<PagingResponse> getTeacherCourse(@RequestParam(defaultValue = "1") final Integer page, Long teacher, LocalDate cosStd, LocalDate cosEtd) {
//
//        final Page<TeacherCourseResponse> courses  = myLectureService.getTeacherCourses(page, teacher, cosStd, cosEtd);
//        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
//        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);
//
//        return ResponseEntity.ok(pagingResponse);
//    }

    /*2. 지난 강의 조회 - 페이징, 종강 일이 이미 지난 날짜일 경우 조회 (강사) */
    @GetMapping("/last_lecture")
    public ResponseEntity<PagingResponse> getTeacherCourseCosEdt(
            @RequestParam(defaultValue = "1") final Integer page,
            @RequestParam final Long teacher
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<TeacherCourseResponse> course = myLectureService.getTeacherCourseCosEdt(page, teacher, currentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(course);
        final PagingResponse pagingResponse = PagingResponse.of(course.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 3. 예정 강의 조회 - 페이징, 개강 일이 아직 지나지 않은 날짜일 경우 조회 (강사) */
    @GetMapping("/scheduled_lecture")
    public ResponseEntity<PagingResponse> getTeacherCourseCosSdt(
            @RequestParam(defaultValue = "1") final Integer page,
            @RequestParam final Long teacher
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<TeacherCourseResponse> course = myLectureService.getTeacherCourseCosSdt(page, teacher, currentDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(course);
        final PagingResponse pagingResponse = PagingResponse.of(course.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


}
