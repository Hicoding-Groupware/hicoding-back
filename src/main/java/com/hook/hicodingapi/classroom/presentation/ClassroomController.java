package com.hook.hicodingapi.classroom.presentation;

import com.hook.hicodingapi.classroom.domain.Classroom;
import com.hook.hicodingapi.classroom.domain.repository.ClassroomRepository;
import com.hook.hicodingapi.classroom.dto.response.ClassroomResponse;
import com.hook.hicodingapi.classroom.service.ClassroomService;
import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.course.dto.resposne.TeacherCoursesResponse;
import com.hook.hicodingapi.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class ClassroomController {

    private final ClassroomService classroomService;


}
