package com.hook.hicodingapi.student.domain.repository;

import com.hook.hicodingapi.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {


    @Query(value = "")
    Page<Student> searchAll(Pageable pageable);
}
