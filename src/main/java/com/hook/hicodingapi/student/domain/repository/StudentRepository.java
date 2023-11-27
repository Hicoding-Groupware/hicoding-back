package com.hook.hicodingapi.student.domain.repository;

import com.hook.hicodingapi.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
