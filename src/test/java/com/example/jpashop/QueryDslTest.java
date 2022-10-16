package com.example.jpashop;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.jpashop.entity.Member;
import com.example.jpashop.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class QueryDslTest {

    @Autowired
    EntityManager em;

    @DisplayName("querydsl 설정 테스트")
    @Test
    void queryDslTest() {
        //given
        Member member = new Member("홍길동홍");
        em.persist(member);

        JPAQueryFactory query = new JPAQueryFactory(em);
        //QMember qHello = new QMember("h");
        QMember qHello = QMember.member;

        //when
        Member findMember = query
            .selectFrom(qHello)
            .fetchOne();

        //then
        assertThat(findMember).isEqualTo(member);
    }

}
