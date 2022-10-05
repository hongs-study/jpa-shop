package com.example.jpashop.repository;

import com.example.jpashop.domain.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    //@PersistenceContext -> 스프링데이터JPA에서는 @Autowired 로 사용 가능. -> 롬복 @RequiredArgsConstructor 사용 가능
    //private EntityManager em;
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }

    public List<Member> findAllByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name ", Member.class)
            .setParameter("name", name)
            .getResultList();
    }
}
