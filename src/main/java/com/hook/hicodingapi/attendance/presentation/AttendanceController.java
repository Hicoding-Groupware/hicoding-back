package com.hook.hicodingapi.attendance.presentation;

import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.attendance.dto.request.AttendanceUpdateRequest;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.attendance.dto.response.MonthAttendanceResponse;
import com.hook.hicodingapi.attendance.service.AttendanceService;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/hc-app/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;


    /* 4. 일별 출석표 조회 */
    @GetMapping("/day/{cosCode}")
    public ResponseEntity<List<DailyAttendanceResponse>> getAttendances(
            @PathVariable final Long cosCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate atdDate,
            @RequestParam(required = false) Long atdCode
    ) {
        LocalDate defaultAtdDate = atdDate != null ? atdDate : LocalDate.now();

        List<DailyAttendanceResponse> dailyAttendanceResponses = studentService.getAttendanceForDay(cosCode, defaultAtdDate, atdCode);

        return ResponseEntity.ok(dailyAttendanceResponses);
    }

    /* 5. 출석 등록 */
    @PostMapping("/day")
    public ResponseEntity<Void> save(@RequestBody @Valid List<AttendanceRegistRequest> registAttendance
    ) {
        /* 등록 데이터 저장 */
        attendanceService.save(registAttendance);

        return ResponseEntity.ok().build();
    }

    /* 6. 출석 수정 */
    @PutMapping("/day/{atdDate}")
    public ResponseEntity<Void> update(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate atdDate,
                                       @RequestBody List<AttendanceUpdateRequest> updateAttendance
    ) {

        attendanceService.update(atdDate, updateAttendance);

        return ResponseEntity.ok().build();

    }

    /* 7. 월별 출석부 조회 */
    @GetMapping("/month/{cosCode}")
    public ResponseEntity<List<MonthAttendanceResponse>> getMonthAttendances(
            @PathVariable final Long cosCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate atdDate
    ) {
        LocalDate currentDate = LocalDate.now();

        if (atdDate == null) {
            atdDate = currentDate;
        }

        LocalDate firstDayOfMonth = atdDate.withDayOfMonth(1);
        LocalDate lastDayOfMonth = atdDate.withDayOfMonth(atdDate.lengthOfMonth());

        List<MonthAttendanceResponse> monthAttendanceResponses = attendanceService.getAttendanceForMonth(cosCode, firstDayOfMonth, lastDayOfMonth, atdDate);

        return ResponseEntity.ok(monthAttendanceResponses);
    }
}
