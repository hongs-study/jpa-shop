package com.example.jpashop.repository;

import com.example.jpashop.domain.entity.Order;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    public Long save(Order order) {
        em.persist(order);
        return order.getId();
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

}
