package com.hook.hicodingapi.record.service;

import com.hook.hicodingapi.common.exception.ConflictException;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.record.domain.repository.RecordCourseRepository;
import com.hook.hicodingapi.record.domain.repository.RecordRepository;
import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import com.hook.hicodingapi.record.dto.request.StudentCosRegistRequest;
import com.hook.hicodingapi.record.dto.request.RecordUpdateRequest;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;

@RequiredArgsConstructor
@Service
@Transactional
public class RecordService {

    private final StudentRepository studentRepository;
    private final RecordRepository recordRepository;
    private final RecordCourseRepository recordCourseRepository;

    public void cosRegist(StudentCosRegistRequest studentRequest) {

        Student student = studentRepository.findById(studentRequest.getStdCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STD_CODE));

        Course course = recordCourseRepository.findById(studentRequest.getCosCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COURSE_CODE));

        /* 해당 강좌 인원 초과시 throw 시킴 */
        if(course.getCurCnt() >= course.getCapacity()) {
            throw new ConflictException(NOT_ENOUGH_CAPACITY);
        }

        /* 현재 수강인원 증가 */
        course.updateCurCnt(course.getCurCnt());

        final Record newRecord = Record.of(
                student,
                course
        );

        recordRepository.save(newRecord);

    }


    public void recordUpdate(Long recCode) {

        Record record = recordRepository.findById(recCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_RECORD_CODE));

        Course course = recordCourseRepository.findById(record.getCourse().getCosCode())
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_COURSE_CODE));

        /* 현재 수강인원 감소 */
        course.downCurcnt(course.getCurCnt());

        record.withdraw();
    }
}