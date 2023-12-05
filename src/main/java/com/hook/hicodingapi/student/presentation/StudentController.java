package com.hook.hicodingapi.student.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentCourse;
import com.hook.hicodingapi.student.dto.response.StudentCourseResponse;
import com.hook.hicodingapi.student.dto.response.StudentsRecordResponse;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class StudentController {

    private final StudentService studentService;

    /* 수강생 등록 */
    @PostMapping("/students")
    public ResponseEntity<Void> regist(@RequestBody @Valid final StudentRegistRequest registRequest) {

        final Long stdCode = studentService.regist(registRequest);

        return ResponseEntity.created(URI.create("/students/" + stdCode)).build();
    }

    /* 수강생 수정 */
    @PutMapping("/students/{stdCode}")
    public ResponseEntity<Void> update(@PathVariable final Long stdCode,
                                       @RequestBody @Valid final StudentUpdateRequest studentRequest) {

        studentService.update(stdCode, studentRequest);

        return ResponseEntity.created(URI.create("/students/" + stdCode)).build();
    }


    /* 원생 조회 및 검색조건 모두 포함 */
    @GetMapping("/students")
    public ResponseEntity<PagingResponse> getMutiSearch(@RequestParam(defaultValue = "1") final Integer page,
                                                  @RequestParam(required = false) final String sort,
                                                  @RequestParam(required = false) final String stdName,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  final LocalDate startDate,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate endDate){

        final Page<StudentsRecordResponse> students = studentService.getMutiSearch(page, sort, stdName, startDate, endDate);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(students);
        final PagingResponse pagingResponse = PagingResponse.of(students.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }


    /* 코스명,담당강사, 코스기간 목록조회 */
    @GetMapping("/students/searchCourse")
    public ResponseEntity<PagingResponse> getCourse(@RequestParam(defaultValue = "1") final Integer page){

        final Page<StudentCourseResponse> courses = studentService.getCourse(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 코스명 조회 */
    @GetMapping("/students/searchCosName")
    public ResponseEntity<PagingResponse> getCourseName(@RequestParam(defaultValue = "1") final Integer page,
                                                        @RequestParam final String cosName) {

        final Page<StudentCourseResponse> courses = studentService.getCourseName(page, cosName);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(courses);
        final PagingResponse pagingResponse = PagingResponse.of(courses.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }
}
