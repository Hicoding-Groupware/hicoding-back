package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentCourse;
import com.hook.hicodingapi.student.dto.response.StudentsRecordResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_STD_CODE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 15);
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
    public Page<StudentsRecordResponse> getStudentsRecord(Integer page) {

        Page<Student> students = studentRepository.findAll(getPageable(page));

        return students.map(student -> StudentsRecordResponse.from(student));
        //return students;
        //return null;
    }

//    @Transactional(readOnly = true)
//    public Page<StudentRepository.StudentRecordSearch> getStudentsRecordByStudentName(Integer page, String studentName) {
//
//        Page<StudentRepository.StudentRecordSearch> students = studentRepository.findByStudentNameContains(getPageable(page), studentName);
//        log.info("students : {}", students.getContent().get(0).getStdName());
//        //log.info("students : {}", students.getContent().get(0).get("STD_NAME"));
//
//        //return students.map(student -> StudentsRecordResponse.from(student));
//        return students;
//    }
//
//    public Page<StudentRepository.StudentRecordSearch> getStudentsRecordByDate(Integer page, Date cosSdt, Date cosEdt) {
//
//        Page<StudentRepository.StudentRecordSearch> students = studentRepository.findByCosDateBetween(getPageable(page), cosSdt, cosEdt);
//        log.info("students : {}", students.getContent().get(0).getCosSdt());
//        return null;
//    }
}
