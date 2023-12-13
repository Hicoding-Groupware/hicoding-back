package com.hook.hicodingapi.msg.presentation;

import com.hook.hicodingapi.common.paging.Pagenation;
import com.hook.hicodingapi.common.paging.PagingButtonInfo;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.msg.dto.request.MessageCreateRequest;
import com.hook.hicodingapi.msg.dto.response.MessageDetailReceiveResponse;
import com.hook.hicodingapi.msg.dto.response.MessageDetailSendResponse;
import com.hook.hicodingapi.msg.dto.response.MessageResponse;
import com.hook.hicodingapi.msg.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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


        messageService.send(messageRequest, msgFile, customUser);


        return ResponseEntity.ok().build();  // URI => 메세지 상세조회용
    }



    /* 파일 다운로드 */
    @GetMapping("/msgs/{fileNo}")
    public ResponseEntity<Resource>downloadFile(@PathVariable Long fileNo, HttpServletRequest request,
                                                @AuthenticationPrincipal CustomUser customUser) throws UnsupportedEncodingException {
        // 파일 접근자랑 일치하는지 여부

         messageService.getEquals(fileNo, customUser);

        String fileName = messageService.getFileName(fileNo);

        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        //fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        Resource resource = messageService.loadFileResource(fileNo);

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /* 받은 쪽지 상세 조회 (조회시 ReadStatus 바뀌고, readAt에 읽은시간 한번만 업데이트)*/
    @GetMapping("/msgs/receiver/{msgNo}")
    public ResponseEntity<MessageDetailReceiveResponse> getDetailMsgs(@PathVariable final Long msgNo,
                                                               @AuthenticationPrincipal CustomUser customUser) {

        final MessageDetailReceiveResponse messageDetailReceiveResponse = messageService.getDetailMsgs(msgNo, customUser);

        return ResponseEntity.ok(messageDetailReceiveResponse);
    }

    /* 보낸 쪽지 상세 조회 */
    @GetMapping("/msgs/sender/{msgNo}")
    public ResponseEntity<MessageDetailSendResponse> getSendDetailMsgs(@PathVariable final Long msgNo,
                                                                       @AuthenticationPrincipal CustomUser customUser) {

        return null;
    }



    /* 받은 쪽지함(로그인한 사람, 삭제 안한 쪽지) */
    @GetMapping("/msgs")
    public ResponseEntity<PagingResponse> getMsgs(
            @RequestParam(defaultValue = "1") final Integer page,
            @AuthenticationPrincipal CustomUser customUser
    ){
        final Page<MessageResponse> msgs = messageService.getMsgs(page, customUser);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(msgs);
        final PagingResponse pagingResponse = PagingResponse.of(msgs.getContent(), pagingButtonInfo);
        return ResponseEntity.ok(pagingResponse);
    }

    /* 보낸 쪽지함(로그인한 사람) */

}
