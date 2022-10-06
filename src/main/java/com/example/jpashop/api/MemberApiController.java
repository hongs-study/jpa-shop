package com.example.jpashop.api;

import com.example.jpashop.domain.entity.Member;
import com.example.jpashop.service.MemberService;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping
    public MemberDetailResponse join(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long memberId = memberService.join(member);

        Member savedMember = memberService.findOne(memberId);
        MemberDetailResponse response = new MemberDetailResponse(savedMember.getId(), savedMember.getName());
        return response;
    }

    @PutMapping("/{memberId}")
    public MemberDetailResponse update(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(memberId, request.getName());

        Member savedMember = memberService.findOne(memberId);
        MemberDetailResponse response = new MemberDetailResponse(savedMember.getId(), savedMember.getName());
        return response;
    }

    @Data
    @AllArgsConstructor
    static class MemberDetailResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty(message = "회원의 이름은 필수값입니다.")
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }
}
