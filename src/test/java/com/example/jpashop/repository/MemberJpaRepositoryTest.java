package com.example.jpashop.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.jpashop.entity.Member;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Rollback(value = false)
@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("멤버 저장&조회 JPA")
    @Test
    void save() {
        //given
        Member member = new Member();
        member.setName("홍길동");

        //when
        Member savedMember = memberJpaRepository.save(member);
//        em.clear();
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getName()).isEqualTo(findMember.getName());
        assertThat(member).isEqualTo(findMember);
    }

}