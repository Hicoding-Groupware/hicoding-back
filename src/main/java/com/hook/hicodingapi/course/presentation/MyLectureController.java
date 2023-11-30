package com.hook.hicodingapi.course.presentation;


import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.resposne.DetailCourseLectureResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCourseResponse;
import com.hook.hicodingapi.course.service.MyLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.hook.hicodingapi.jwt.CustomUser;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1/my_lecture")
public class MyLectureController {

    private final MyLectureService myLectureService;

    /* 1. 진행 중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
    @GetMapping("/in_progress")
    public ResponseEntity<PagingResponse> getTeacherCourse(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<TeacherCourseResponse> courses  = myLectureService.getTeacherCourseCosSdtAndCosEdt(page, customUser, currentDate, currentDate);

        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /*2. 지난 강의 조회 - 페이징, 종강 일이 이미 지난 날짜일 경우 조회 (강사) */
    @GetMapping("/last_lecture")
    public ResponseEntity<PagingResponse> getTeacherCourseCosEdt(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<TeacherCourseResponse> course = myLectureService.getTeacherCourseCosEdt(page, customUser, currentDate);

        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(course);
        final PagingResponse pagingResponse = PagingResponse.of(course.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /* 3. 예정 강의 조회 - 페이징, 개강 일이 아직 지나지 않은 날짜일 경우 조회 (강사) */
    @GetMapping("/scheduled_lecture")
    public ResponseEntity<PagingResponse> getTeacherCourseCosSdt(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
            ) {
        LocalDate currentDate = LocalDate.now();

        final Page<TeacherCourseResponse> course = myLectureService.getTeacherCourseCosSdt(page, customUser, currentDate);

        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(course);
        final PagingResponse pagingResponse = PagingResponse.of(course.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    /* 4. 강의 상세 조회 - 조건 없이 해당 강의 상세 목록 모두 조회 가능 (강사) */
    @GetMapping("/detail_info") // @PathVariable 로 나중에 바꾸기
    public ResponseEntity<DetailCourseLectureResponse> getTeacherCosCode(
            @RequestParam final Long cosCode
    ) {

        final DetailCourseLectureResponse detailCourseLectureResponse = myLectureService.getTeacherCosCode(cosCode);

        return ResponseEntity.ok(detailCourseLectureResponse);
    }

}
