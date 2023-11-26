package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    public Long regist(StudentRegistRequest registRequest) {

        final Student newStudent = Student.of(
                registRequest.getStdName(),
                registRequest.getStdGender(),
                registRequest.getStdBirth(),
                registRequest.getStdPhone(),
                registRequest.getStdEmail(),
                registRequest.getPostNo(),
                registRequest.getAddress(),
                registRequest.getDetailAddress(),
                registRequest.getStdMemo()
        );

        final Student student = studentRepository.save(newStudent);

        return student.getStdCode();
    }
}
