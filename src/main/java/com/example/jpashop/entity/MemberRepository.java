package com.example.jpashop.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // SpringBoot 가 알아서 자동으로 EntityManager 생성 & 설정정보(application.yml)읽어서 객체를 생성해준다.
    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
