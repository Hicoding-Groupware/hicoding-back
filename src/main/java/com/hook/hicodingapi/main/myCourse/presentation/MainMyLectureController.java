package com.hook.hicodingapi.main.myCourse.presentation;


import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.resposne.DetailCourseLectureResponse;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.main.myCourse.service.MainMyLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1/lecture")
public class MainMyLectureController {

    private final MainMyLectureService mainMyLectureService;

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
    @GetMapping("/mainProgress")
    public ResponseEntity<PagingResponse> getTeacherCourse(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<DetailCourseLectureResponse> courses = mainMyLectureService.getTeacherCourseCosSdtAndCosEdt(page, customUser, currentDate, currentDate);


        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

}
