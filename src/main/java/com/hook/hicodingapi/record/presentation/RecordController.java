package com.hook.hicodingapi.record.presentation;

import com.hook.hicodingapi.record.dto.request.StudentCosRegistRequest;
import com.hook.hicodingapi.record.dto.response.RecordListResponse;
import com.hook.hicodingapi.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hc-app/v1")
public class RecordController {

    private final RecordService recordService;

    /* 수강 등록 */
    @PostMapping("/students/record")
    public ResponseEntity<Void> cosRegist(@RequestBody @Valid final StudentCosRegistRequest studentRequest) {

        recordService.cosRegist(studentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 수강 철회시 상태 값 변경 및 현재 수강인원 감소 및 철회 일자 insert */
    @PutMapping("/students/record/{recCode}")
    public ResponseEntity<Void> recordUpdate(@PathVariable final Long recCode){

        recordService.recordUpdate(recCode);

       return ResponseEntity.created(URI.create("/students/record/" + recCode)).build();
    }

    /* 수강 조회 */
    @GetMapping("/students/record/{stdCode}")
    public ResponseEntity<List<RecordListResponse>> getRecordList(@PathVariable final Long stdCode) {

        List<RecordListResponse> recordList = recordService.getRecordList(stdCode);

        return ResponseEntity.ok(recordList);
    }



}
