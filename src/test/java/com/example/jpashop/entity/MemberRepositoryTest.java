package com.example.jpashop.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @PersistenceContext
    private EntityManager em;

    @Rollback(value = false)
    @Transactional
    @DisplayName("회원저장과 조회 테스트")
    @Test
    void memberTest() {
        //given
        Member member = new Member();
        member.setName("홍길동1");

        //when
        Long savedMemberId = memberRepository.save(member);
        em.clear();
        Member findedMember = memberRepository.find(savedMemberId);

        //then
        Assertions.assertThat(findedMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findedMember.getName()).isEqualTo(member.getName());
    }

}