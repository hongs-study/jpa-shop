package com.example.jpashop.repository.order.query;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {
        // 1.메인 정보를 먼저 DTO조회한다
        List<OrderQueryDto> orders = getOrders();

        // 2.조회된 메인정보 수만큼 for문을 돌면서 컬렉션정보를 조회 및 세팅한다
        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new com.example.jpashop.repository.order.query.OrderItemQueryDto("
                + "oi.order.id, i.name, oi.orderPrice, oi.count)"
            + " from OrderItem oi"
            + " join oi.item i"
            + " where oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }

    private List<OrderQueryDto> getOrders() {
        return em.createQuery(
                "select new com.example.jpashop.repository.order.query.OrderQueryDto("
                    + "o.id, m.name, o.orderDate, o.status, d.address"
                    + ")"
                    + " from Order o"
                    + " join o.member m"
                    + " join o.delivery d"
                , OrderQueryDto.class)
            .getResultList();
    }

}
