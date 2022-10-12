package com.example.jpashop.repository;

import com.example.jpashop.entity.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 복잡한 쿼리를 위한 별도 Repository!
 * 중요한 비지니스로직, 커맨드 쿼리가 아닌, 단순히 복잡한 조회 쿼리는 이렇게 별도로 리파지토리를 만드는 것이
 * 추후 유지보수 측면에서 유리하다.
 */
@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembersQuery() {
        return em.createQuery("select m from Member m")
            .getResultList();
    }

}
