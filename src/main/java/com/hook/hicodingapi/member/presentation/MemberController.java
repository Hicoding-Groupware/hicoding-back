package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import com.hook.hicodingapi.member.dto.request.MemberRandomCreationRequest;
import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
import com.hook.hicodingapi.member.dto.request.MemberInformationRequest;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;


import java.util.List;

@RestController
@RequestMapping(BASE_PATH + MEMBER_PATH)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 직원 전체 조회
    @GetMapping("/all")
    public ResponseEntity<List<Member>> getAllMembers() {

        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 직원 상세 조회
    @GetMapping()
    public ResponseEntity<List<Member>> getDetailMembers(final MemberInquiryRequest memberInquiryRequest) {

        List<Member> members = memberService.getDetailMembers(memberInquiryRequest);
        return ResponseEntity.ok(members);
    }

    /* 2. 개인정보 업데이트 */
    @PutMapping("information/{memberNo}")
    public ResponseEntity<Void> information(@PathVariable final Long memberNo,
                                            @RequestPart @Valid final MemberInformationRequest informationRequest,
                                            @RequestPart(required = false) final MultipartFile multipartFile) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 직원 생성
    @PostMapping()
    public ResponseEntity<List<MemberCreationResponse>> insert(@RequestBody @Valid MemberCreationRequest memberCreationRequest) {

        List<MemberCreationResponse> memberCreationResponseList = new ArrayList<>();

        for (int i = 0; i < memberCreationRequest.getCnt(); i++)
            memberService.customInsert(memberCreationRequest, memberCreationResponseList);

        return ResponseEntity.ok(memberCreationResponseList);
    }

    // 직원 랜덤 생성
    @PostMapping("/random")
    public ResponseEntity<Void> randomInsert(@RequestBody @Valid MemberRandomCreationRequest memberRandomCreationRequest) {

        final String mbrPwd = memberRandomCreationRequest.getMemberPwd();
        for (int i = 0; i < memberRandomCreationRequest.getMbrCnt(); i++)
            memberService.randomInsert(mbrPwd);

        return ResponseEntity.ok().build();
    }

    // 직원 수정
    @PutMapping("/{memberCode}")
    public ResponseEntity<Void> update(@PathVariable final Long memberCode,
                                       @RequestBody HashMap<String, Object> hash) {

        MemberRole memberRole = MemberRole.valueOf((String) hash.get("memberRole"));
        MemberStatus memberStatus = MemberStatus.valueOf((String) hash.get("memberStatus"));
        memberService.update(memberCode, memberRole, memberStatus);

        return ResponseEntity.ok().build();
    }


    // 직원 삭제
    @DeleteMapping("/{memberCode}")
    public ResponseEntity<Void> delete(@PathVariable("memberCode") final Long memberCode) {

        memberService.deleteMember(memberCode);

        return ResponseEntity.noContent().build();
    }

    // 직원 전체 삭제
    @DeleteMapping("/all")
    public ResponseEntity<Void> allDelete() {

        memberService.deleteAllMembers();

        return ResponseEntity.noContent().build();
    }
}
