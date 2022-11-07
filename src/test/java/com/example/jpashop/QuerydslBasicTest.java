package com.example.jpashop;

import com.example.jpashop.querydsl.entity.Member;
import com.example.jpashop.querydsl.entity.QMember;
import com.example.jpashop.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("[jpql VS querydsl 비교] member1을 찾아라")
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    // 1.com.querydsl.jpa.impl.JPAQueryFactory 에 엔티티메니저를 넣어 인스턴스가 필요하다
    // 필드 공유해도 됨 => 멀티쓰레드 환경에서도 동시성 문제 없음. 트랜젝션 단위로 독단적으로 작동함.
    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void beforeAll() {
        jpaQueryFactory = new JPAQueryFactory(em);

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
    }

    @DisplayName("JPQL")
    @Test
    void test1Jpql() {
        //
        String userName = "멤버1";
        Member member1 = em.createQuery(
            "select m from Member m "
                + "where m.userName = :userName",
                Member.class)
            .setParameter("userName", userName)
            .getSingleResult();

        Assertions.assertThat(member1.getUserName()).isEqualTo(userName);
    }
    @DisplayName("querydsl")
    @Test
    void test1Querydsl() {

        // 2.사실 Q객체는 이미 생성되어있기 때문에 그대로 사용하면 된다
        QMember m = new QMember("m");

        String userName = "멤버1";

        // 장점1.코드로작성=>컴파일시점 오류발생
        // 장점2.파라미터 자동 바인딩
        Member member1 = jpaQueryFactory.select(m).from(m)
            .where(m.userName.eq(userName))
            .fetchOne();

        Assertions.assertThat(member1.getUserName()).isEqualTo(userName);
    }
}
