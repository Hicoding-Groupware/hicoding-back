package com.hook.hicodingapi.student.service;

import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.dto.request.StudentRegistRequest;
import com.hook.hicodingapi.student.dto.request.StudentUpdateRequest;
import com.hook.hicodingapi.student.dto.response.StudentCourseResponse;
import com.hook.hicodingapi.student.dto.response.StudentsRecordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_STD_CODE;
import static com.hook.hicodingapi.course.domain.type.CourseStatusType.AVAILABLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;


    private Pageable getStudentPageable(final Integer page) {
        return PageRequest.of(page - 1, 15, Sort.by("stdCode").descending());
    }
    private Pageable getCoursePageable(final Integer page) {
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

        Page<Student> students = studentRepository.findAll(getStudentPageable(page));

        return students.map(student -> StudentsRecordResponse.from(student));
    }

    @Transactional(readOnly = true)
    public Page<StudentsRecordResponse> getStudentName(Integer page, String studentName) {

        Page<Student> students = studentRepository.findByStdNameContaining(getStudentPageable(page), studentName);

        return students.map(student -> StudentsRecordResponse.from(student));
    }

    @Transactional(readOnly = true)
    public Page<StudentsRecordResponse> getCreatedAt(Integer page, LocalDate startDate, LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        Page<Student> students = studentRepository.findByCreatedAtBetween(getStudentPageable(page), startDateTime, endDateTime);

        return students.map(student -> StudentsRecordResponse.from(student));
    }

    @Transactional(readOnly = true)
    public Page<StudentCourseResponse> getCourse(Integer page) {

        Page<Course> courses = courseRepository.findByStatus(getCoursePageable(page), AVAILABLE);

        return courses.map(course -> StudentCourseResponse.from(course));
    }

    @Transactional(readOnly = true)
    public Page<StudentCourseResponse> getCourseName(Integer page, String cosName) {

        Page<Course> courses = courseRepository.findByCosNameContainsAndStatus(getCoursePageable(page), cosName, AVAILABLE);

        return courses.map(course -> StudentCourseResponse.from(course));
    }


    @Transactional
    public List<DailyAttendanceResponse> getAttendanceForDay(Long cosCode) {

        List<Student> students = studentRepository.findStudentsByAndSignupStatus();

        return students.stream()
                .map(DailyAttendanceResponse::from)
                .collect(Collectors.toList());
    }
}
