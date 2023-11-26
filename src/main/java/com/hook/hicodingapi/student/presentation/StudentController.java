package com.hook.hicodingapi.student.presentation;

import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class StudentController {

    private final StudentService studentService;

    /* 학생 등록 */
    @PostMapping("/students")
    public ResponseEntity<Void> regist(@RequestBody @Valid final StudentRegistRequest registRequest) {

        final Long stdCode = studentService.regist(registRequest);

        return ResponseEntity.created(URI.create("/students/" + stdCode)).build();
    }
}
