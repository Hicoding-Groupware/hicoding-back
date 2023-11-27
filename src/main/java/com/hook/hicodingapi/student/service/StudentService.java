package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_STD_CODE;

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

    public void update(Long stdCode, StudentUpdateRequest studentRequest) {

        Student student = studentRepository.findById(stdCode)
                .orElseThrow(()-> new NotFoundException(NOT_FOUND_STD_CODE));

        student.update(
                studentRequest.getStdName(),
                studentRequest.getStdGender(),
                studentRequest.getStdBirth(),
                studentRequest.getStdPhone(),
                studentRequest.getStdEmail(),
                studentRequest.getPostNo(),
                studentRequest.getAddress(),
                studentRequest.getDetailAddress(),
                studentRequest.getStdMemo()
        );
    }
}
