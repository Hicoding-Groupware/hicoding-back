package com.hook.hicodingapi.student.domain.repository;

import com.hook.hicodingapi.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByStdNameContaining(Pageable pageable, String studentName);
    Page<Student> findByCreatedAtBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);





   /* @Query(value = "SELECT STD_CODE as StdCode, STD_NAME as StdName, STD_BIRTH as StdBirth, " +
            "COS_NAME as CosName, MEMBER_NAME as MemberName, COS_SDT as CosSdt, COS_EDT as CosEdt, STD_PHONE as StdPhone, REGISTED_DATE as RegistedDate\n" +
            "FROM (\n" +
            "         SELECT\n" +
            "             s.STD_CODE, s.STD_NAME, s.STD_BIRTH, c.COS_NAME, m.MEMBER_NAME, c.COS_SDT, c.COS_EDT, s.STD_PHONE, r.REGISTED_DATE,\n" +
            "             ROW_NUMBER() OVER (PARTITION BY s.STD_CODE ORDER BY r.REGISTED_DATE DESC) AS row_num\n" +
            "         FROM tbl_student s\n" +
            "                  JOIN tbl_record r ON s.STD_CODE = r.STD_CODE\n" +
            "                  JOIN tbl_course c ON r.COS_CODE = c.COS_CODE\n" +
            "                  JOIN tbl_member m ON c.TEACHER = m.MEMBER_NO\n" +
            "     ) AS search\n" +
            "WHERE row_num = 1\n" +
            "ORDER BY STD_CODE",
            countQuery = "SELECT COUNT(*)\n" +
                    "FROM (\n" +
                    "         SELECT\n" +
                    "             s.STD_CODE, s.STD_NAME, s.STD_BIRTH, c.COS_NAME, m.MEMBER_NAME, c.COS_SDT, c.COS_EDT, s.STD_PHONE, r.REGISTED_DATE,\n" +
                    "             ROW_NUMBER() OVER (PARTITION BY s.STD_CODE ORDER BY r.REGISTED_DATE DESC) AS row_num\n" +
                    "         FROM tbl_student s\n" +
                    "                  JOIN tbl_record r ON s.STD_CODE = r.STD_CODE\n" +
                    "                  JOIN tbl_course c ON r.COS_CODE = c.COS_CODE\n" +
                    "                  JOIN tbl_member m ON c.TEACHER = m.MEMBER_NO\n" +
                    "     ) AS search\n" +
                    "WHERE row_num = 1",
            nativeQuery = true)
    Page<StudentRecordSearch> searchAll(Pageable pageable);
    interface StudentRecordSearch {

        Long getStdCode();
        String getStdName();
        Date getStdBirth();
        String getCosName();
        String getMemberName();
        Date getCosSdt();
        Date getCosEdt();
        String getStdPhone();
        Date getRegistedDate();
    }*/
}
