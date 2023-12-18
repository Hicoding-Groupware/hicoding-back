package com.hook.hicodingapi.lecture.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.resposne.CourseDetailResponse;
import com.hook.hicodingapi.lecture.dto.request.LectureCreateRequest;
import com.hook.hicodingapi.lecture.dto.request.LectureUpdateRequest;
import com.hook.hicodingapi.lecture.dto.response.LectureDetailResponse;
import com.hook.hicodingapi.lecture.dto.response.TeacherLecturesResponse;
import com.hook.hicodingapi.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lectures") //전체 강의 조회(강사)
    public ResponseEntity<PagingResponse> getTeacherLectures(@RequestParam(defaultValue = "1") Integer page){

        final Page<TeacherLecturesResponse> lectures = lectureService.getTeacherLectures(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(lectures);
        final PagingResponse pagingResponse = PagingResponse.of(lectures.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/lectures/{lecCode}")//강의 상세 조회
    public ResponseEntity<LectureDetailResponse> getLectureDetail(@PathVariable final Long lecCode) {

        final LectureDetailResponse lectureDetailResponse = lectureService.getLectureDetail(lecCode);

        return ResponseEntity.ok(lectureDetailResponse);
    }


    @PostMapping("/lectures") //신규 강의 생성(행정)
    public ResponseEntity<Void> save(@RequestBody @Valid final LectureCreateRequest lectureRequest){

        final Long lecCode = lectureService.save(lectureRequest);

        return ResponseEntity.created(URI.create("/lectures-management/" + lecCode)).build();
    }

    @PutMapping("/lectures/{lecCode}")//강의 수정
    public ResponseEntity<Void> update (@PathVariable final Long lecCode,
                                        @RequestBody @Valid final LectureUpdateRequest lectureRequest){

        lectureService.update(lecCode, lectureRequest);

        return ResponseEntity.created(URI.create("/lecture-management/" + lecCode)).build();

    }

    @DeleteMapping("/lectures/{lecCode}")//강의 삭제
    public ResponseEntity<Void> delete(@PathVariable final Long lecCode){

        lectureService.delete(lecCode);

        return ResponseEntity.noContent().build();
    }
}
