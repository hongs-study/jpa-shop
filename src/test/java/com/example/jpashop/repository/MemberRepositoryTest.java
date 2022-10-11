package com.example.jpashop.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.jpashop.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    @DisplayName("멤버 CRUD")
    @Test
    void crudTest() {
        //given
        Member member1 = new Member("회원1");
        Member member2 = new Member("회원2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건조회
        Member findMem1 = memberRepository.findById(member1.getId()).get();
        Member findMem2 = memberRepository.findById(member2.getId()).get();
        assertThat(member1).isEqualTo(findMem1);
        assertThat(member2).isEqualTo(findMem2);

        findMem1.setName("변경!!");

        // 다건조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 건수조회
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long afterDelCount = memberRepository.count();
        assertThat(afterDelCount).isEqualTo(0);

    }

    @DisplayName("메소드이름으로쿼리생성 - 스프링JPA사용해보기 - JPA직접사용과 동일한 기능 확인")
    @Test
    void testQuery() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findTop3HelloBy();

        //then
        assertThat(members.size()).isGreaterThan(1);

    }

    @DisplayName("쿼리메서드기능2-JPA Named Query")
    @Test
    void jpaNamedQuery() {
        //given
        Member member1 = new Member("회원1", 10, null);
        Member member2 = new Member("회원1", 20, null);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByUserName("회원1");

        //then
        assertThat(members.get(0).getName()).isEqualTo("회원1");
    }
}
