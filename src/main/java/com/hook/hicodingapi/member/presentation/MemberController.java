package com.hook.hicodingapi.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;


@RestController
@RequestMapping(BASE_PATH + MEMBER_PATH)
public class MemberController {
    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        System.out.println("테스트");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
