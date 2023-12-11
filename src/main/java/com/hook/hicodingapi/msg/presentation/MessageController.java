package com.hook.hicodingapi.msg.presentation;

import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.msg.dto.request.MessageCreateRequest;
import com.hook.hicodingapi.msg.dto.response.MessageResponse;
import com.hook.hicodingapi.msg.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/hc-app/v1")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /* 쪽지 쓰기 */
    @PostMapping("/msgs")
    public ResponseEntity<Void> send(
            @RequestPart(required = false) @Valid final MessageCreateRequest messageRequest,
            @RequestPart(required = false) final MultipartFile msgFile,
            @AuthenticationPrincipal CustomUser customUser) {

        final Long msgCode = messageService.send(messageRequest, msgFile, customUser);

        return ResponseEntity.created(URI.create("/msgs/" + msgCode)).build();  // URI => 메세지 상세조회용
    }


    /* 받은 쪽지함(로그인한 사람) */
    @GetMapping("/msgs")
    public ResponseEntity<PagingResponse> getMsgs(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
    ){
        final Page<MessageResponse> msgs = messageService.getMsgs(page, customUser);
        return null;
    }

}
