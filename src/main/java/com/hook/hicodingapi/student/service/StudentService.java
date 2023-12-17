package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.common.exception.ConflictException;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentCourse;
import com.hook.hicodingapi.student.dto.response.StudentCourseResponse;
import com.hook.hicodingapi.student.dto.response.StudentDetailResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;
import static com.hook.hicodingapi.course.domain.type.CourseStatusType.AVAILABLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;


    private Pageable getStudentDscPageable(final Integer page) {
        return PageRequest.of(page - 1, 15, Sort.by("stdCode").descending());
    }

    private Pageable getStudentAscPageable(final Integer page) {
        return PageRequest.of(page - 1, 15, Sort.by("stdCode").ascending());
    }

    private Pageable getStudentPageable(final Integer page, Sort.Direction direction) {
        return PageRequest.of(page - 1, 15, Sort.by(direction, "stdCode"));
    }

    private Pageable getCoursePageable(final Integer page) {
        return PageRequest.of(page - 1, 5);
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
    public Page<StudentCourseResponse> getCourse(Integer page) {

        Page<Course> courses = courseRepository.findByStatus(getCoursePageable(page), AVAILABLE);

        return courses.map(course -> StudentCourseResponse.from(course));
    }

    @Transactional(readOnly = true)
    public Page<StudentCourseResponse> getCourseName(Integer page, String cosName, LocalDate currentDay) {

        Page<Course> resultCourses = courseRepository.findByCosNameContainsAndStatusAndCosEdtAfter(
                getCoursePageable(page), cosName, AVAILABLE, currentDay);
        return resultCourses.map(courseList -> StudentCourseResponse.from(courseList));

    }


    @Transactional
    public List<DailyAttendanceResponse> getAttendanceForDay(Long cosCode) {

        List<Student> students = studentRepository.findStudentsByAndSignupStatus();

        return students.stream()
                .map(DailyAttendanceResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<StudentsRecordResponse> getMultiSearch(Integer page, String sort, String stdName, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }

        // 오래된 등록순
        if("asc".equals(sort) && stdName == null && startDate == null && endDate == null) {
            Page<Student> students = studentRepository.findAll(getStudentPageable(page, Sort.Direction.ASC));
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근등록순
        } else if("desc".equals(sort) && stdName == null && startDate == null && endDate == null) {
            Page<Student> students = studentRepository.findAll(getStudentPageable(page, Sort.Direction.DESC));
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 이름 조회
        } else if ("asc".equals(sort) && startDate == null && endDate == null) {
            Page<Student> students = studentRepository.findByStdNameContaining(getStudentPageable(page, Sort.Direction.ASC), stdName);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 이름 조회
        } else if ("desc".equals(sort) && startDate == null && endDate == null) {
            Page<Student> students = studentRepository.findByStdNameContaining(getStudentPageable(page, Sort.Direction.DESC), stdName);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 등록 시작날짜 조회
        } else if ("asc".equals(sort) && stdName == null && endDate == null) {
            Page<Student> students = studentRepository.findByCreatedAtGreaterThanEqual(getStudentPageable(page, Sort.Direction.ASC), startDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 등록 이전날짜 조회
        } else if ("asc".equals(sort) && stdName == null && startDate == null) {
            Page<Student> students = studentRepository.findByCreatedAtLessThanEqual(getStudentPageable(page, Sort.Direction.ASC), endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 등록 기간 조회
        } else if ("asc".equals(sort) && stdName == null) {
            Page<Student> students = studentRepository.findByCreatedAtBetween(getStudentPageable(page, Sort.Direction.ASC), startDateTime, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 이름조회, 등록 시작날짜 조회
        } else if ("asc".equals(sort) && endDate == null) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtGreaterThanEqual(getStudentPageable(page, Sort.Direction.ASC), stdName, startDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 이름조회, 등록 이전날짜 조회
        } else if ("asc".equals(sort) && startDate == null) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtLessThanEqual(getStudentPageable(page, Sort.Direction.ASC), stdName, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 오래된 등록순, 이름조회, 등록 기간 조회
        } else if ("asc".equals(sort)) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtBetween(getStudentPageable(page, Sort.Direction.ASC), stdName, startDateTime, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 등록 시작날짜 조회
        } else if ("desc".equals(sort) && stdName == null && endDate == null) {
            Page<Student> students = studentRepository.findByCreatedAtGreaterThanEqual(getStudentPageable(page, Sort.Direction.DESC), startDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 등록 이전날짜 조회
        } else if ("desc".equals(sort) && stdName == null && startDate == null) {
            Page<Student> students = studentRepository.findByCreatedAtLessThanEqual(getStudentPageable(page, Sort.Direction.DESC), endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 등록 기간 조회
        } else if ("desc".equals(sort) && stdName == null) {
            Page<Student> students = studentRepository.findByCreatedAtBetween(getStudentPageable(page, Sort.Direction.DESC), startDateTime, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 이름조회, 등록 시작날짜 조회
        } else if ("desc".equals(sort) && endDate == null) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtGreaterThanEqual(getStudentPageable(page, Sort.Direction.DESC), stdName, startDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 이름조회, 등록 이전날짜 조회
        } else if ("desc".equals(sort) && startDate == null) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtLessThanEqual(getStudentPageable(page, Sort.Direction.DESC), stdName, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
            // 최근 등록순, 이름조회, 등록 기간 조회
        } else if ("desc".equals(sort)) {
            Page<Student> students = studentRepository.findByStdNameContainingAndCreatedAtBetween(getStudentPageable(page, Sort.Direction.DESC), stdName, startDateTime, endDateTime);
            return students.map(student -> StudentsRecordResponse.from(student));
        } else {
            Page<Student> students = studentRepository.findAll(getStudentPageable(page, Sort.Direction.DESC));
            return students.map(student -> StudentsRecordResponse.from(student));
        }




    }

    @Transactional(readOnly = true)
    public StudentDetailResponse getStudent(final Long stdCode) {

        Student student = studentRepository.findById(stdCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STD_CODE));

        return StudentDetailResponse.from(student);
    }
}
