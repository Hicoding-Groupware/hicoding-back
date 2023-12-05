package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.dto.request.MemberUpdateRequest;
import com.hook.hicodingapi.member.dto.response.PreLoginResponse;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;


    /*--------------------- 민서 존 ------------------------------------------*/
    @PostMapping("/pre/login")
    public ResponseEntity<PreLoginResponse> preLogin(@RequestBody Map<String, String> loginInfo) {

        PreLoginResponse preLoginResponse = memberService.preLogin(loginInfo);

        return ResponseEntity.ok(preLoginResponse);
    }

    @PutMapping("/member")
    public ResponseEntity<Void> update(@RequestBody final MemberUpdateRequest memberUpdateRequest){

        memberService.memberUpdate(memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
