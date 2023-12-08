package com.hook.hicodingapi.attendance.presentation;

import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.attendance.service.AttendanceService;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate atdDate
    ) {
        LocalDate defaultAtdDate = atdDate != null ? atdDate : LocalDate.now();

        List<DailyAttendanceResponse> dailyAttendanceResponses = studentService.getAttendanceForDay(cosCode, defaultAtdDate);

        return ResponseEntity.ok(dailyAttendanceResponses);
    }

    /* 5. 출석 등록 */
    @PostMapping("/day")
    public ResponseEntity<Void> save(@RequestBody @Valid AttendanceRegistRequest registAttendance,
                                     @AuthenticationPrincipal CustomUser customUser
    ) {
        /* 출결 데이터 미등록 확인 */
        attendanceService.validateAttendanceCreate(registAttendance.getStatus(), customUser);

        /* 등록 데이터 저장 */
        attendanceService.save(registAttendance, customUser);

        return ResponseEntity.ok().build();
    }


}
