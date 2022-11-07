package com.example.jpashop.querydsl.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
class MemberTest {

    @Autowired
    EntityManager em;

    @DisplayName("멤버 엔티티 테스트")
    @Test
    void testMember() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member 멤버1 = new Member("멤버1", 10, teamA);
        Member 멤버2 = new Member("멤버2", 10, teamA);
        Member 멤버3 = new Member("멤버3", 10, teamB);
        Member 멤버4 = new Member("멤버4", 10, teamB);
        em.persist(멤버1);
        em.persist(멤버2);
        em.persist(멤버3);
        em.persist(멤버4);

        em.flush();
        em.clear();

        //when
        List<Member> members = em.createQuery("select m from Member m", Member.class)
            .getResultList();

        //then
        members.forEach(m -> {
            System.out.println("member : " + m.toString());
            System.out.println("team : " + m.getTeam().toString());
        });

    }

}