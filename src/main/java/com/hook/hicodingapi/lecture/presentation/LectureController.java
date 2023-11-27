package com.hook.hicodingapi.lecture.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.lecture.dto.response.TeacherLecturesResponse;
import com.hook.hicodingapi.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures")
    public ResponseEntity<PagingResponse> getTeacherLectures(@RequestParam(defaultValue = "1") Integer page){

        final Page<TeacherLecturesResponse> lectures = lectureService.getTeacherLectures(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(lectures);
        final PagingResponse pagingResponse = PagingResponse.of(lectures.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
}
