package com.hook.hicodingapi.attendance.service;

import com.hook.hicodingapi.attendance.domain.repository.AttendanceRepository;
import com.hook.hicodingapi.course.domain.repository.MyLectureRepository;
import com.hook.hicodingapi.student.domain.Student;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MyLectureRepository myLectureRepository;
    private final StudentRepository studentRepository;




//    /* 일일 출석표 조회 */
//    @Transactional(readOnly = true)
//    public List<DayAttendanceResponse> getAttendanceForDay(final Long cosCode, final LocalDate atdDate) {
//        List<Attendance> attendanceList = attendanceRepository.findByCosCodeCosCodeAndAtdDate(cosCode, atdDate);
////        List<DayAttendanceResponse> responseList = new ArrayList<>();
////        for (Attendance attendance : attendanceList) {
////            responseList.add(DayAttendanceResponse.from(attendance));
////        }
////        return responseList;
//        // 코드 개선 스트림 + 람다 표현식
//        // stream() 메소드를 호출하여 attendanceList를 스트림으로 변환한 후, map 연산자를 사용하여
//        // 각 Attendance 객체를 DayAttendanceResponse.from 메소드로 매핑하고, 마지막으로 collect 메소드를 사용하여
//        // 결과를 List로 수집한다.
//
//        return attendanceList.stream()
//                .map(DayAttendanceResponse::from)
//                .collect(Collectors.toList());
//    }

}