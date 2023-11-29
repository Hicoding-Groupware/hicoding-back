package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.dto.request.MemberGenerateRequest;
import com.hook.hicodingapi.member.dto.response.MemberGenerateResponse;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;


@RestController
@RequestMapping(BASE_PATH + MEMBER_PATH)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        System.out.println("테스트");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 직원 생성
    @PostMapping("/create")
    public ResponseEntity<List<MemberGenerateResponse>> create(@RequestBody @Valid MemberGenerateRequest memberGenerateRequest) {

        List<MemberGenerateResponse> memberGenerateResponseList = new ArrayList<>();

        memberService.create(memberGenerateRequest, memberGenerateResponseList);

        return ResponseEntity.ok(memberGenerateResponseList);
    }


}
