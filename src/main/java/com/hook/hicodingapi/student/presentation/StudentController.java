package com.hook.hicodingapi.student.presentation;

import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
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
}