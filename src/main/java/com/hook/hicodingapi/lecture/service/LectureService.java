package com.hook.hicodingapi.lecture.service;

import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.domain.repository.LectureRepository;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
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
}
