package com.hook.hicodingapi.course.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.dto.request.CourseCreateRequest;
import com.hook.hicodingapi.course.dto.request.CourseUpdateRequest;
import com.hook.hicodingapi.course.dto.resposne.CourseDetailResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("courses")//전체 과정 (리스트)
    public ResponseEntity<List<TeacherCoursesResponse>> getExpectedCourses() {
        final List<TeacherCoursesResponse> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }


    @GetMapping("courses-proceeding")//과정 조회(진행중)
    public ResponseEntity<PagingResponse> getProceedingCourses(@RequestParam(defaultValue = "1") Integer page) {

        final Page<TeacherCoursesResponse> courses = courseService.getProceedingCourses(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("courses-expected")//과정 조회(예정)
    public ResponseEntity<PagingResponse> getExpectedCourses(@RequestParam(defaultValue = "1") Integer page) {

        final Page<TeacherCoursesResponse> courses = courseService.getExpectedCourses(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }


    @GetMapping("/courses/{cosCode}")//과정 상세 조회
    public ResponseEntity<CourseDetailResponse> getCourseDetail(@PathVariable final Long cosCode) {

        final CourseDetailResponse courseDetailResponse = courseService.getCourseDetail(cosCode);

        return ResponseEntity.ok(courseDetailResponse);
    }


    @PostMapping("/courses") //과정 생성
    public ResponseEntity<Void> save(@RequestBody @Valid final CourseCreateRequest courseRequest){

        final Long cosCode = courseService.save(courseRequest);

        return ResponseEntity.created(URI.create("/courses-management/" + cosCode)).build();
    }

    @PutMapping("/courses/{cosCode}")//과정 수정
    public ResponseEntity<Void> update (@PathVariable final Long cosCode,
                                        @RequestBody @Valid final CourseUpdateRequest courseRequest){

        courseService.update(cosCode, courseRequest);

        return ResponseEntity.created(URI.create("/courses/" + cosCode)).build();
    }

    @DeleteMapping("/courses/{cosCode}")//과정 삭제
    public ResponseEntity<Void> delete(@PathVariable final Long cosCode){

        courseService.delete(cosCode);

        return ResponseEntity.noContent().build();
    }

}
