package com.example.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jpashop.domain.entity.Member;
import com.example.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional // 테스트코드 내에서는 rollback=true 이 기본값이다
@SpringBootTest
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;


    @DisplayName("[회원가입] 성공")
    @Test
    void saveMember() {
        //given
        Member member = new Member();
        member.setName("아이셔");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(savedId));

    }

    @DisplayName("같은 이름이 있으면 예외 발생")
    @Test
    void exceptionSameName() {
        //given
        Member member1 = new Member();
        member1.setName("아이셔");
        Member member2 = new Member();
        member2.setName("아이셔");

        //when & then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
            () -> {
                memberService.join(member1);
                memberService.join(member2);
            }, "[!!테스트 실패!!] 이미 같은 이름의 회원이 있다는 예외가 발생해야 한다.");
        assertEquals("이미 존재하는 회원입니다", illegalStateException.getMessage());
    }
}