package com.hook.hicodingapi.main.message.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.main.message.response.MainMessageResponse;
import com.hook.hicodingapi.main.message.service.MainMessageService;
import com.hook.hicodingapi.msg.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class MainMessageController {

    private final MainMessageService mainMessageService;

    /* 받은 쪽지함(로그인한 사람, 삭제 안한 쪽지) */
    @GetMapping("/mainMsgs")
    public ResponseEntity<PagingResponse> getMsgs(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
    ){
        final Page<MainMessageResponse> msgs = mainMessageService.getMsgs(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(msgs);
        final PagingResponse pagingResponse = PagingResponse.of(msgs.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }
}
