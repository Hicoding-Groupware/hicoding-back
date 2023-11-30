package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import com.hook.hicodingapi.member.dto.request.MemberRandomCreationRequest;
import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;


@RestController
@RequestMapping(BASE_PATH + MEMBER_PATH)
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 직원 전체 조회
    @GetMapping("/search/all")
    public ResponseEntity<List<Member>> getAllMembers() {

        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // 직원 상세 조회
    @GetMapping("/search")
    public ResponseEntity<List<Member>> getDetailMembers(final MemberInquiryRequest memberInquiryRequest) {

        List<Member> members = memberService.getDetailMembers(memberInquiryRequest);
        return ResponseEntity.ok(members);
    }

    // 직원 생성
    @PostMapping("/creation")
    public ResponseEntity<List<MemberCreationResponse>> insert(@RequestBody @Valid MemberCreationRequest memberCreationRequest) {

        List<MemberCreationResponse> memberCreationResponseList = new ArrayList<>();

        for (int i = 0; i < memberCreationRequest.getCnt(); i++)
            memberService.customInsert(memberCreationRequest, memberCreationResponseList);

        return ResponseEntity.ok(memberCreationResponseList);
    }

    // 직원 랜덤 생성
    @PostMapping("/creation/random")
    public ResponseEntity<Void> randomInsert(@RequestBody @Valid MemberRandomCreationRequest memberRandomCreationRequest) {

        final String mbrPwd = memberRandomCreationRequest.getMemberPwd();
        for (int i = 0; i < memberRandomCreationRequest.getMbrCnt(); i++)
            memberService.randomInsert(mbrPwd);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 직원 삭제
    @DeleteMapping("/deletion/{memberCode}")
    public ResponseEntity<Void> delete(@PathVariable("memberCode") final Long memberCode) {

        memberService.deleteMember(memberCode);

        return ResponseEntity.noContent().build();
    }

    // 직원 전체 삭제
    @DeleteMapping("/deletion/all")
    public ResponseEntity<Void> allDelete() {

        memberService.deleteAllMembers();

        return ResponseEntity.noContent().build();
    }
}
