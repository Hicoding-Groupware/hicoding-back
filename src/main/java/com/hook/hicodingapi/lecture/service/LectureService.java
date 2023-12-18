package com.hook.hicodingapi.lecture.service;

import com.hook.hicodingapi.common.exception.BadRequestException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.dto.resposne.CourseDetailResponse;
import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.domain.repository.LectureRepository;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import com.hook.hicodingapi.lecture.dto.request.LectureCreateRequest;
import com.hook.hicodingapi.lecture.dto.request.LectureUpdateRequest;
import com.hook.hicodingapi.lecture.dto.response.LectureDetailResponse;
import com.hook.hicodingapi.lecture.dto.response.TeacherLecturesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_COS_CODE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_LEC_CODE;
import static com.hook.hicodingapi.lecture.domain.type.LectureStatusType.AVAILABLE;
import static com.hook.hicodingapi.lecture.domain.type.LectureStatusType.DELETED;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    private Pageable getPageable(final Integer page){

        return PageRequest.of(page-1,  10, Sort.by("lecCode"));
    }

    @Transactional(readOnly = true)
    public Page<TeacherLecturesResponse> getTeacherLectures(final Integer page){

        Page<Lecture> lectures = lectureRepository.findByStatus(getPageable(page), AVAILABLE);

        return lectures.map(lecture -> TeacherLecturesResponse.from(lecture));
    }


    //강의등록
    public Long save(LectureCreateRequest lectureRequest) {

        final Lecture newLecture = Lecture.of(
                lectureRequest.getLecName(),
                lectureRequest.getTextbook(),
                lectureRequest.getTechStack()
        );

        final Lecture lecture = lectureRepository.save(newLecture);

        return lecture.getLecCode();
    }

    //강의 수정
    public void update(final Long lecCode, final LectureUpdateRequest lectureRequest) {

        Lecture lecture = lectureRepository.findByLecCodeAndStatusNot(lecCode,DELETED)
                .orElseThrow(()->new BadRequestException(NOT_FOUND_LEC_CODE));

        lecture.update(
                lectureRequest.getLecName(),
                lectureRequest.getTextbook(),
                lectureRequest.getTechStack()
        );
    }

    //강의 삭제
    public void delete(final Long lecCode) {

        lectureRepository.deleteById(lecCode);
    }


    //강의 상세조회
    public LectureDetailResponse getLectureDetail(Long lecCode) {
        Lecture lecture = lectureRepository.findByLecCodeAndStatus(lecCode, LectureStatusType.AVAILABLE)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_LEC_CODE));

        return LectureDetailResponse.from(lecture);
    }
}
