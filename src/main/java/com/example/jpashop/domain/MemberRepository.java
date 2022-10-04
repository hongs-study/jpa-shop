package com.example.jpashop.domain.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    // SpringBoot 가 알아서 자동으로 EntityManager 생성 & 설정정보(application.yml)읽어서 객체를 생성해준다.
    @PersistenceContext
    private EntityManager em;

    public Long save(com.example.jpashop.domain.entity.Member member) {
        em.persist(member);
        return member.getId();
    }

    public com.example.jpashop.domain.entity.Member find(Long id) {
        return em.find(com.example.jpashop.domain.entity.Member.class, id);
    }

}
