package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.dto.request.MemberInformationRequest;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /* 2. 개인정보 업데이트 */
    @PutMapping("information/{memberNo}")
    public ResponseEntity<Void> information(@PathVariable final Long memberNo,
                                            @RequestPart @Valid final MemberInformationRequest informationRequest,
                                            @RequestPart(required = false) final MultipartFile multipartFile){


        return ResponseEntity.created(URI.create("/" + memberNo)).build();
    }

}
