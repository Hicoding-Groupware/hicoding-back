package com.hook.hicodingapi.course.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.request.CourseCreateRequest;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.course.service.CourseService;
import com.hook.hicodingapi.lecture.dto.request.LectureCreateRequest;
import com.hook.hicodingapi.lecture.dto.request.LectureUpdateRequest;
import com.hook.hicodingapi.lecture.dto.response.TeacherLecturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class CourseController {

    private final CourseService courseService;

    //과정 조회(강사)
    @GetMapping("courses")
    public ResponseEntity<PagingResponse> getTeacherCourses(@RequestParam(defaultValue = "1") Integer page) {

        final Page<TeacherCoursesResponse> courses = courseService.getTeacherCourses(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    //과정 생성
    @PostMapping("/courses")
    public ResponseEntity<Void> save(@RequestBody @Valid final CourseCreateRequest courseRequest){

        final Long cosCode = courseService.save(courseRequest);

        return ResponseEntity.created(URI.create("/lectures-management/" + cosCode)).build();
    }

    //과정 수정

    //과정 삭제
    @DeleteMapping("/courses/{cosCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long cosCode){

        courseService.delete(cosCode);

        return ResponseEntity.noContent().build();
    }
}
