package com.hook.hicodingapi.attendance.presentation;


import com.hook.hicodingapi.attendance.dto.response.DayAttendanceResponse;
import com.hook.hicodingapi.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hc-app/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /* 일일 출석표 조회 */
    @GetMapping("/day/{cosCode}/{cosDate}")
    public ResponseEntity<List<DayAttendanceResponse>> getAttendances(
            @PathVariable final Long cosCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cosDate) {
            // @인증 로그인 한 유저 인증 해야 하나?
            // LocalDate currentDate = LocalDate.now();

      final List<DayAttendanceResponse> dayAttendanceResponse = attendanceService.getAttendanceForDay(cosCode, cosDate);

      return ResponseEntity.ok(dayAttendanceResponse);
    }

}
