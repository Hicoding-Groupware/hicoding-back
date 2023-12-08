package com.hook.hicodingapi.attendance.presentation;


import com.hook.hicodingapi.attendance.dto.request.AttendanceRegistRequest;
import com.hook.hicodingapi.attendance.dto.response.DailyAttendanceResponse;
import com.hook.hicodingapi.attendance.service.AttendanceService;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.course.domain.repository.CourseRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.student.domain.repository.StudentRepository;
import com.hook.hicodingapi.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hc-app/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final CourseRepository courseRepository;


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
    public ResponseEntity<Void> save(@RequestParam @Valid final AttendanceRegistRequest registAttendance,
                                     @AuthenticationPrincipal CustomUser customUser
    ) {
        /* 등록 데이터 저장 */
//        final Long atdCode = attendanceService.save(registAttendance);

//        return ResponseEntity.created(URI.create("/hc-app/v1/attendance" + atdCode)).build();
        // http 201 Create 상태 코드를 나타낸다. 새로운 리소스가 성공적으로 생성되었음을 나타낸다.
        // URI.create("hc-app/v1/attendance" + atdCode)는 새로 생성된 출석 데이터의 url를 나타낸다.
        // 만약 여러개의 출석 데이터를 만들 경우 각 데이터에 대해 새로운 url이 생성된다.
        // 클라이언트가 이러한 다수의 url을 관리하기 어렵다고 생각되면, 예를 들어 특정 기간 동안의 출석 데이터를
        // 한 번에 요청할 수 있는 엔드포인트를 제공하거나, 페이징 등의 기술을 사용하여 결과를 제한하는 방법을 고려할 수 있다.

        return ResponseEntity.ok().build();
    }


}
