package com.example.jpashop.repository.order.simplequery;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtoList() {
        return em.createQuery(
                "select new com.example.jpashop.repository.order.simplequery.OrderSimpleQueryDto("
                    + "o.id"
                    + ", m.name"
                    + ", d.address"
                    + ", o.orderDate"
                    + ", o.status"
                    + ")"
                    + " from Order o"
                    + " join o.member m"
                    + " join o.delivery d", OrderSimpleQueryDto.class)
            .getResultList();
    }
}
