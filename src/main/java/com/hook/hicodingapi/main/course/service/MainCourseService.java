package com.hook.hicodingapi.main.course.service;

import com.hook.hicodingapi.classroom.domain.Classroom;
import com.hook.hicodingapi.classroom.domain.repository.ClassroomRepository;
import com.hook.hicodingapi.common.exception.BadRequestException;
import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.dto.request.CourseCreateRequest;
import com.hook.hicodingapi.course.dto.request.CourseUpdateRequest;
import com.hook.hicodingapi.course.dto.resposne.CourseDetailResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.domain.repository.LectureRepository;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_COS_CODE;

@Service
@Transactional
@RequiredArgsConstructor
public class MainCourseService {

    private final CourseRepository courseRepository;


    private LocalDate sdt = LocalDate.now();
    private LocalDate edt = LocalDate.now();

    private Pageable getPageable(final Integer page){

        return PageRequest.of(page-1,  6, Sort.by("cosCode").descending());
    }

    //과정 조회(진행중)
    @Transactional(readOnly = true)
    public Page<TeacherCoursesResponse> getProceedingCourses(final Integer page){

        Page<Course> courses = courseRepository.findByStatusNotAndCosSdtLessThanEqualAndCosEdtGreaterThanEqual(getPageable(page), CourseStatusType.DELETED, sdt, edt);

        return courses.map(course -> TeacherCoursesResponse.from(course));
    }

    //과정 조회(예정)
    @Transactional(readOnly = true)
    public Page<TeacherCoursesResponse> getExpectedCourses(final Integer page){

        Page<Course> courses = courseRepository.findByStatusNotAndCosSdtGreaterThan(getPageable(page), CourseStatusType.DELETED, sdt);

        return courses.map(course -> TeacherCoursesResponse.from(course));
    }




}
