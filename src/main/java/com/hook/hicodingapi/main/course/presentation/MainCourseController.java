package com.hook.hicodingapi.main.course.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.main.course.service.MainCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class MainCourseController {

    private final MainCourseService maincourseService;

    @GetMapping("main-courses-proceeding")//과정 조회(진행중)
    public ResponseEntity<PagingResponse> getProceedingCourses(@RequestParam(defaultValue = "1") Integer page) {

        final Page<TeacherCoursesResponse> courses = maincourseService.getProceedingCourses(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("main-courses-expected")//과정 조회(예정)
    public ResponseEntity<PagingResponse> getExpectedCourses(@RequestParam(defaultValue = "1") Integer page) {

        final Page<TeacherCoursesResponse> courses = maincourseService.getExpectedCourses(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }



}
