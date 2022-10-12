package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

/**
 * 주의-규칙! 스프링부트2 이후부터는 사용자정의인터페이스명 + Impl 도 지원한다!
 */
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
            .getResultList();
    }
}
