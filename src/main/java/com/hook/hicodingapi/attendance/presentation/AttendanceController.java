package com.hook.hicodingapi.attendance.presentation;


import com.hook.hicodingapi.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hc-app/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /* 일일 출석표 조회 */
//    @GetMapping("/day/{cosCode}/{cosDate}")
//    public ResponseEntity<List<DayAttendanceResponse>> getAttendances(
//            @PathVariable final Long cosCode,
//            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cosDate)
//            // {cosDate}는 문자열 형식의 날짜로 전달, 이를 LocalDate로 변환하려면 컨트롤러 메소드의
//            // @PathVariable 어노테이션에 @DateTimeFormat을 사용할 수 있다.
//    {
//      final List<DayAttendanceResponse> dayAttendanceResponse = attendanceService.getAttendanceForDay(cosCode, cosDate);
//
//      return ResponseEntity.ok(dayAttendanceResponse);
//    }


    /* 5. 일별 출석표 조회 */


}
