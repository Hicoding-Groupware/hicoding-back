package com.hook.hicodingapi.student.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentsRecordResponse;
import com.hook.hicodingapi.student.dto.response.StudentsResponse;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    /* 수강생 목록 조회 */
    @GetMapping("/students")
    public ResponseEntity<PagingResponse> getStudents(@RequestParam(defaultValue = "1") final Integer page
                                                     ) {

        final Page<StudentsResponse> students = studentService.getStudents(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(students);
        final PagingResponse pagingResponse = PagingResponse.of(students.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    /* 최근 수강등록한 강좌 한개만 나오게 원생 목록 조회 */
    @GetMapping("/studentsRecord")
    public ResponseEntity<PagingResponse> getStudentsRecord(@RequestParam(defaultValue = "1") final Integer page) {

        final Page<StudentRepository.StudentRecordSearch> studentsRecord = studentService.getStudentsRecord(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(studentsRecord);
        final PagingResponse pagingResponse = PagingResponse.of(studentsRecord.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }
}
