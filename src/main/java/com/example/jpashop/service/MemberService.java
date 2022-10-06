package com.example.jpashop.service;

import com.example.jpashop.domain.entity.Member;
import com.example.jpashop.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        Long id = memberRepository.save(member);
        return id;
    }

    private void validateDuplicateMember(Member member) {
        List<Member> sameNamedMember = memberRepository.findAllByName(member.getName());
        if (sameNamedMember.size() > 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 1명 조회
    public Member findOne(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public void updateMember(Long memberId, String memberName) {
        Member member = memberRepository.findById(memberId);
        member.setName(memberName);
    }
}
