package com.hook.hicodingapi.attendance.presentation;


import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.attendance.service.AttendanceService;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hc-app/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;


    /* 5. 일별 출석표 조회 */
    @GetMapping("/day/{cosCode}")
    public ResponseEntity<List<DailyAttendanceResponse>> getAttendances(
            @PathVariable final Long cosCode
    ) {
        final List<DailyAttendanceResponse> dailyAttendanceResponses = studentService.getAttendanceForDay(cosCode);

        return ResponseEntity.ok(dailyAttendanceResponses);
    }

    /* 6. 출석 등록 */
//    @PostMapping("/day")
//    public ResponseEntity<Void> save(@RequestParam @Valid final AttendanceRegistRequest registAttendance) {

        /* 등록 데이터 저장 */
//        final Long atdCode = attendanceService.save(registAttendance);
//
//        return ResponseEntity.created(URI.create("/hc-app/v1/attendance" + atdCode)).build();
//    }


}
