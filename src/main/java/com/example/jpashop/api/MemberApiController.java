package com.example.jpashop.api;

import com.example.jpashop.entity.Member;
import com.example.jpashop.repository.MemberDto;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.service.MemberService;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final MemberRepository memberRepository;

    @GetMapping("/test/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getName();
    }

    @GetMapping("/test2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getName();
    }

    //@PostConstruct
    public void init() {
        for (int i=0; i<100; i++) {
            memberRepository.save(new Member("회원" + i, i, null));
        }
    }

    @GetMapping("/members/page")
    public Page<MemberDto> findMembersPage(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }



    @PostMapping
    public MemberResponse join(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(request.getName());

        Long memberId = memberService.join(member);

        Member savedMember = memberService.findOne(memberId);
        MemberResponse response = new MemberResponse(savedMember.getId(), savedMember.getName());
        return response;
    }

    @PutMapping("/{memberId}")
    public MemberResponse update(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.updateMember(memberId, request.getName());

        Member savedMember = memberService.findOne(memberId);
        MemberResponse response = new MemberResponse(savedMember.getId(), savedMember.getName());
        return response;
    }

    @GetMapping
    public Result<List<MemberResponse>> searchAll() {
        List<MemberResponse> memberResponses = memberService.findMembers().stream()
            .map(m -> new MemberResponse(m.getId(), m.getName()))
            .toList();
        return new Result<>(memberResponses.size(), memberResponses);
    }

    @Data
    @AllArgsConstructor
    static class MemberResponse {
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

    @Data
    static class Result<T> {
        private int count;
        private T data;

        public Result(int count, T data) {
            this.count = count;
            this.data = data;
        }

        public Result(int count, Iterable<?> memberResponses) {
            this.count = count;
            this.data = (T) memberResponses;
        }
    }
}
