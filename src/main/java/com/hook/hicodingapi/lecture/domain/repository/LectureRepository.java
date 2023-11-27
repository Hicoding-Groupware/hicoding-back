package com.hook.hicodingapi.lecture.domain.repository;

import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Page<Lecture> findByStatus(Pageable pageable, LectureStatusType lectureStatusType);
}
