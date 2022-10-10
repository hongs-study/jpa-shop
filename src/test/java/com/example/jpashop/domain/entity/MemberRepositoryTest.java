package com.example.jpashop.domain.entity;

import com.example.jpashop.entity.Member;
import com.example.jpashop.repository.MemberRepository;
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

        System.out.println("memberRepository = " + memberRepository.getClass());

        //given
        Member member = new Member("홍길동1");

        //when
        Member savedMember = memberRepository.save(member);
        em.clear();
        Member findMember = memberRepository.findById(savedMember.getId())
            .orElseThrow(() -> new IllegalStateException("멤버가 조회되지 않습니다."));

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    }

}