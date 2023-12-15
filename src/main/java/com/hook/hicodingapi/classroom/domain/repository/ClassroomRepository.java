package com.hook.hicodingapi.classroom.domain.repository;

import com.hook.hicodingapi.classroom.domain.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

}
