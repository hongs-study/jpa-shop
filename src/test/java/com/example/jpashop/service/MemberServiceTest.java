package com.example.jpashop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jpashop.entity.Member;
import com.example.jpashop.entity.Team;
import com.example.jpashop.repository.MemberRepositoryOld;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@Rollback(value = false)
@Transactional // 테스트코드 내에서는 rollback=true 이 기본값이다
@SpringBootTest
class MemberServiceTest {

    @Autowired private EntityManager em;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepositoryOld memberRepository;


    @DisplayName("[회원가입] 성공")
    @Test
    void saveMember() {
        //given
        Member member = new Member("아이셔");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(savedId));

    }

    @DisplayName("같은 이름이 있으면 예외 발생")
    @Test
    void exceptionSameName() {
        //given
        Member member1 = new Member("아이셔");
        Member member2 = new Member("아이셔");

        //when & then
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
            () -> {
                memberService.join(member1);
                memberService.join(member2);
            }, "[!!테스트 실패!!] 이미 같은 이름의 회원이 있다는 예외가 발생해야 한다.");
        assertEquals("이미 존재하는 회원입니다", illegalStateException.getMessage());
    }

    @DisplayName("멤버, 팀 테스트")
    @Test
    void teamAndMemberTest() {
        //given
        Team team1 = new Team("팀1");
        Team team2 = new Team("팀3");
        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("회원1", 10, team1);
        Member member2 = new Member("회원2", 20, team1);
        Member member3 = new Member("회원3", 30, team2);
        Member member4 = new Member("회원4", 40, team2);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        //when
        List<Member> members = em.createQuery("select m from Member m", Member.class)
            .getResultList();

        //then
        for (Member m : members) {
            System.out.println("member = " + m);
            System.out.println("member team = " + m.getTeam());
        }
    }
}