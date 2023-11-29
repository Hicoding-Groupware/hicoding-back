package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
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

    // 직원 생성
    @PostMapping("/members")
    public ResponseEntity<List<MemberCreationResponse>> insert(@RequestBody @Valid MemberCreationRequest memberCreationRequest) {

        List<MemberCreationResponse> memberCreationResponseList = new ArrayList<>();

        for (int i = 0; i < memberCreationRequest.getCnt(); i++)
            memberService.customInsert(memberCreationRequest, memberCreationResponseList);

        return ResponseEntity.ok(memberCreationResponseList);
    }

    // 직원 랜덤 생성
    @GetMapping("/members-random")
    public ResponseEntity<Void> randomInsert() {

        memberService.randomInsert("1234");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 직원 전체 조회
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers() {

        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 직원 상세 조회
    @PostMapping("/members-detail")
    public ResponseEntity<List<Member>> getDetailMembers(@RequestBody final MemberInquiryRequest memberInquiryRequest) {

        List<Member> members = memberService.getDetailMembers(memberInquiryRequest);
        return ResponseEntity.ok(members);
    }

    // 직원 전체 삭제
    @DeleteMapping("/allDelete")
    public ResponseEntity<Void> allDelete() {

        memberService.deleteAllMembers();

        //return ResponseEntity.noContent().build(); 아래 코드 ??
        return ResponseEntity.created(URI.create("/" + memberNo)).build();
    }

}
