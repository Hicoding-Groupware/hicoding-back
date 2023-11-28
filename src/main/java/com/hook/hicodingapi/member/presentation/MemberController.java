package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.dto.MemberGenerateRequest;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody @Valid MemberGenerateRequest memberGenerateRequest) {

        memberService.create(memberGenerateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
