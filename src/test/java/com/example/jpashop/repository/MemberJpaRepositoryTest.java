package com.example.jpashop.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.jpashop.entity.Member;
import java.util.List;
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
        Member member = new Member("홍길동1");

        //when
        Member savedMember = memberJpaRepository.save(member);
//        em.clear();
        Member findMember = memberJpaRepository.findById(savedMember.getId()).orElseThrow();

        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getName()).isEqualTo(findMember.getName());
        assertThat(member).isEqualTo(findMember);
    }


    @DisplayName("멤버 CRUD")
    @Test
    void crudTest() {
        //given
        Member member1 = new Member("회원1");
        Member member2 = new Member("회원2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건조회
        Member findMem1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMem2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(member1).isEqualTo(findMem1);
        assertThat(member2).isEqualTo(findMem2);

        findMem1.setName("변경!!");

        // 다건조회
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 건수조회
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long afterDelCount = memberJpaRepository.count();
        assertThat(afterDelCount).isEqualTo(0);

    }
}