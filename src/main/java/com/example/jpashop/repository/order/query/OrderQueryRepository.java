package com.example.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;

    // V4. N+1문제발생
    public List<OrderQueryDto> findAllByDto() {
        // 1.메인 정보를 먼저 DTO조회한다
        List<OrderQueryDto> orders = findOrders();

        // 2.조회된 메인정보 수만큼 for문을 돌면서 컬렉션정보를 조회 및 세팅한다
        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return orders;
    }

    // v5
    public List<OrderQueryDto> findAllByDto_optimization() {
        // 1.메인데이터 우선 조회 - N개 데이터
        List<OrderQueryDto> orders = findOrders();

        // 2.메인데이터에서 ID만 추출해서 서브데이터 in 조건으로 조회 - N번 쿼리 발생하는 문제 해결
        List<Long> orderIds = orders.stream().map(o -> o.getOrderId()).toList();
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new com.example.jpashop.repository.order.query.OrderItemQueryDto("
                    + "oi.order.id, i.name, oi.orderPrice, oi.count)"
                    + " from OrderItem oi"
                    + " join oi.item i"
                    + " where oi.order.id in :orderIds"
                , OrderItemQueryDto.class)
            .setParameter("orderIds", orderIds)
            .getResultList();

        // 3.메모리에서 메인데이터-서브데이터 합체
        // 3-1. 서브데이터를 ID로 groupBy 해줌
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
            .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        // 3-2. 메인데이터 for 돌면서 서브데이터 set.
        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        // 개선된 점
        // 쿼리 N+1 -> 2번으로 줄임
        return orders;
    }

    //v6
    public List<OrderFlatDto> findAllByDto_flat() {

        List<OrderFlatDto> flatDtos = em.createQuery(
                "select new com.example.jpashop.repository.order.query.OrderFlatDto("
                    + "o.id, m.name, o.orderDate, o.status, d.address"
                    + ", i.name, oi.orderPrice, oi.count"
                    + ")"
                    + " from Order o"
                    + " join o.member m"
                    + " join o.delivery d"
                    + " join o.orderItems oi"
                    + " join oi.item i"
                , OrderFlatDto.class)
            .getResultList();

        return flatDtos;
    }






    private List<OrderQueryDto> findOrders() {
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

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new com.example.jpashop.repository.order.query.OrderItemQueryDto("
                    + "oi.order.id, i.name, oi.orderPrice, oi.count)"
                    + " from OrderItem oi"
                    + " join oi.item i"
                    + " where oi.order.id = :orderId", OrderItemQueryDto.class)
            .setParameter("orderId", orderId)
            .getResultList();
    }
}
