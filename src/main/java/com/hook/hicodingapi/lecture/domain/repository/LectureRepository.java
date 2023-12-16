package com.hook.hicodingapi.lecture.domain.repository;

import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Page<Lecture> findByStatus(Pageable pageable, LectureStatusType lectureStatusType);

    Optional<Lecture> findByLecCodeAndStatusNot(Long lecCode, LectureStatusType lectureStatusType);

}
