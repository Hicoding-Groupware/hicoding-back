package com.hook.hicodingapi.course.service;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.dto.response.TeacherLecturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hook.hicodingapi.lecture.domain.type.LectureStatusType.AVAILABLE;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    private Pageable getPageable(final Integer page){

        return PageRequest.of(page-1,  10, Sort.by("cosCode"));
    }
    @Transactional(readOnly = true)
    public Page<TeacherCoursesResponse> getTeacherCourses(final Integer page){

        Page<Course> courses = courseRepository.findByStatus(getPageable(page), CourseStatusType.AVAILABLE);

        return courses.map(course -> TeacherCoursesResponse.from(course));
    }
}
