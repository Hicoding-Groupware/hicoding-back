package com.hook.hicodingapi.course.service;

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
import java.time.LocalDateTime;
import java.util.List;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_COS_CODE;
import static com.hook.hicodingapi.course.domain.type.CourseStatusType.AVAILABLE;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ClassroomRepository classroomRepository;

    private LocalDate sdt = LocalDate.now();
    private LocalDate edt = LocalDate.now();

    private Pageable getPageable(final Integer page){

        return PageRequest.of(page-1,  5, Sort.by("cosCode").descending());
    }


    //모든 과정 조회
    @Transactional(readOnly = true)
    public List<Course> getCourses() {
        return courseRepository.findByStatus( AVAILABLE);
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

    //과정 상세 조회
    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseDetail(Long cosCode) {

        Course course = courseRepository.findByCosCodeAndStatus(cosCode, AVAILABLE)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_COS_CODE));

        return CourseDetailResponse.from(course);
    }

    //과정 등록
    public Long save(CourseCreateRequest courseRequest) {

        Lecture lecture = lectureRepository.findById(courseRequest.getLecCode())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_LEC_CODE));
        Member teacher = memberRepository.findById(courseRequest.getTeacher())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_CODE));
        Member staff = memberRepository.findById(courseRequest.getTeacher())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_CODE));
        Classroom  classroom = classroomRepository.findById(courseRequest.getRoomCode())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_ROOM_CODE));

        final Course newCourse = Course.of(
                courseRequest.getCosName(),
                lecture,
                teacher,
                staff,
                classroom,
                courseRequest.getCosSdt(),
                courseRequest.getCosEdt(),
                courseRequest.getCapacity(),
                courseRequest.getCosNotice(),
                courseRequest.getDayStatus(),
                courseRequest.getTimeStatus()
        );

        final Course course = courseRepository.save(newCourse);

        return course.getCosCode();
    }

    //과정 수정
    public void update(final Long cosCode, final CourseUpdateRequest courseRequest) {

        Course course = courseRepository.findByCosCodeAndStatusNot(cosCode,CourseStatusType.DELETED)
                .orElseThrow(()->new BadRequestException(NOT_FOUND_COS_CODE));
        Lecture lecture = lectureRepository.findById(courseRequest.getLecCode())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_LEC_CODE));
        Member teacher = memberRepository.findById(courseRequest.getTeacher())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_CODE));
        Member staff = memberRepository.findById(courseRequest.getTeacher())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_CODE));
        Classroom  classroom = classroomRepository.findById(courseRequest.getRoomCode())
                .orElseThrow(()->new BadRequestException(ExceptionCode.NOT_FOUND_ROOM_CODE));

        course.update(
                courseRequest.getCosName(),
                lecture,
                teacher,
                staff,
                classroom,
                courseRequest.getCosSdt(),
                courseRequest.getCosEdt(),
                courseRequest.getCapacity(),
                courseRequest.getCurCnt(),
                courseRequest.getDayStatus(),
                courseRequest.getTimeStatus(),
                courseRequest.getCosNotice()
        );
    }

    //과정 삭제
    public void delete(final Long cosCode) {

        courseRepository.deleteById(cosCode);
    }

}
