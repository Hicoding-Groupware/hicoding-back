package com.hook.hicodingapi.mylecture.presentation;

import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.mylecture.service.MyLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mylecture")
public class MyLectureController {

    private final MyLectureService myLectureService;

    /* 1. 진행중인 강의 조회 - 페이징, 개강일 ~ 종강일 사이의 날짜 포함하여 조회 (강사) */
//    @GetMapping("/in-progress")
//    public ResponseEntity<PagingResponse> getTeacherMyLectureInProgress(@RequestParam(defaultValue = "1") final Integer page) {
//
//
//    }

}
