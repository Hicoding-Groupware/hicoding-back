package com.hook.hicodingapi.member.presentation;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.MemberDataSender;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.*;

import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
import com.hook.hicodingapi.member.dto.request.MemberInformationRequest;
import com.hook.hicodingapi.member.dto.response.MemberInquiryResponse;
import com.hook.hicodingapi.member.dto.response.PreLoginResponse;
import com.hook.hicodingapi.member.dto.response.ProfileResponse;
import com.hook.hicodingapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;
import static com.hook.hicodingapi.informationProvider.service.InformationProviderService.generateRandomEnumTypeValue;

import java.util.Map;

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
    public ResponseEntity<List<MemberInquiryResponse>> getDetailMembers(final MemberInquiryRequest memberInquiryRequest) {

        List<MemberInquiryResponse> memberInquiryResponses = memberService.getDetailMembers(memberInquiryRequest);
        return ResponseEntity.ok(memberInquiryResponses);
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
        for (int i = 0; i < memberCreationRequest.getCnt(); i++) {
            memberService.customInsert(memberCreationRequest, memberCreationResponseList);
        }

        return ResponseEntity.ok(memberCreationResponseList);
    }

    // 직원 랜덤 생성
    @PostMapping("/random")
    public ResponseEntity<List<Member>> randomInsert(@RequestBody @Valid MemberRandomCreationRequest memberRandomCreationRequest) {

        List<Member> memberList = new ArrayList<>();

        final String mbrPwd = memberRandomCreationRequest.getMemberPwd();
        for (int i = 0; i < memberRandomCreationRequest.getMbrCnt(); i++) {
            memberList.add(memberService.randomInsert(mbrPwd));
        }

        return ResponseEntity.ok(memberList);
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


    /*---------------------- 민서존 -------------------------------------------------------------------------------------------------------------------*/

    /* 로그인 */
    @PostMapping("/pre/login")
    public ResponseEntity<PreLoginResponse> preLogin(@RequestBody Map<String, String> loginInfo) {

        PreLoginResponse preLoginResponse = memberService.preLogin(loginInfo);

        return ResponseEntity.ok(preLoginResponse);
    }

    @PostMapping("/img")
    public ResponseEntity<Void> save(@RequestPart final MemberProfileUpdate memberProfileUpdate,
                                     @RequestPart final MultipartFile memberProfile){

        memberService.save(memberProfile, memberProfileUpdate);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @PutMapping("/memberInfo")
    public ResponseEntity<Void> update(@RequestBody final MemberUpdateRequest memberUpdateRequest){

        memberService.memberUpdate(memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }


    /*사진만 업데이트*/
   @PutMapping("/memberProfile")
   public ResponseEntity<Void> update(@RequestPart final MemberProfileUpdate memberProfileUpdate,
                                      @RequestPart(required = false) final MultipartFile memberProfile){

       memberService.memberProfileUpdate(memberProfileUpdate, memberProfile);

       return ResponseEntity.status(HttpStatus.CREATED).build();

   }



    /* 비밀 번호 제외 하고 업데이트 */
    @PutMapping("/memberInfowithoutPassword")
    public ResponseEntity<Void> update(@RequestBody final MemberUpdateRequestWithoutPassword memberUpdateRequestWithoutPassword){

        memberService.memberUpdateWithoutPassword(memberUpdateRequestWithoutPassword);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /* 마이페이지 조회 및 수정 */

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal User user){
        ProfileResponse profileResponse = memberService.getProfile(user.getUsername());

        return ResponseEntity.ok(profileResponse);
    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user){

        memberService.deleteProfile(user.getUsername());

        return ResponseEntity.noContent().build();
    }

    //동한
    @GetMapping("/memberList")
    public List<Member> getMemberList(){
        return memberService.getMemberList();

    }






}
