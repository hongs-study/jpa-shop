package com.example.jpashop.entity;

import com.example.jpashop.repository.MemberRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @DisplayName("[Auditing]순수JPA로 구현하기")
    @Test
    void JpaEventBaseEntity() throws InterruptedException {
        //given
        Member member = new Member("회원1");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(1000);
        member.setName("회원2");

        em.flush(); // @PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("createdDate = " + findMember.getCreatedDate());
        System.out.println("updatedDate = " + findMember.getUpdatedDate());

    }

}