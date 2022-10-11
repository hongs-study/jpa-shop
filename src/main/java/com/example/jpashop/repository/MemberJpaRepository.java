package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m").getResultList();
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    // 쿼리메서드기능1 - JPA직접작성 (참고) => MemberRepository.java에서 쉽게 사용 가능
    public List<Member> findByNameAndAgeGreaterThan(String username, int userage) {
        return em.createQuery("select m from Member m where m.name = :username and m.age > :userage")
            .setParameter("username", username)
            .setParameter("userage", userage)
            .getResultList();
    }

    // 쿼리메서드기능2 - JpaNameQuery (참고) => MemberRepository.java에서 쉽게 사용 가능
    public List<Member> findByUserName(String userName) {
        return em.createNamedQuery("Member.findByUserName", Member.class)
            .setParameter("userName", userName)
            .getResultList();
    }

    // 페이징
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age"
                + " order by m.name desc ")
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
            .setParameter("age", age)
            .getSingleResult();
    }
}
