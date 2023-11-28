package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentsRecordResponse;
import com.hook.hicodingapi.student.dto.response.StudentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_STD_CODE;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 15, Sort.by("stdCode").descending());
    }
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


    @Transactional(readOnly = true)
    public Page<StudentsResponse> getStudents(final Integer page) {

        Page<Student> students = studentRepository.findAll(getPageable(page));

        return students.map(student -> StudentsResponse.from(student));
    }

    public Page<StudentsRecordResponse> getStudentsRecord(Integer page) {

        Page<Student> studentsRecord = studentRepository.searchAll(getPageable(page));

        return null;
    }
}
