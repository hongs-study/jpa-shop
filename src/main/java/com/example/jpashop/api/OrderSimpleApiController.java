package com.example.jpashop.api;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import com.example.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v4/simple-orders")
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V4 - DTO로 직접조회 (V3과 트레이드오프)
     * Tip.
     *  - 사실 DTO로 직접조회의 성능개서는 엄청 크지 않다.
     *  - 또, V3-엔티티조회가 활용성이 더 크다.
     *  - 따라서, V3-엔티티조회 를 기본으로 사용하다가 -> 필요할 때 V4-DTO로 직접조회를 선택적으로 써라.
     */
    @GetMapping
    public List<OrderSimpleQueryDto> ordersV4() {
        List<OrderSimpleQueryDto> orderDtoList = orderSimpleQueryRepository.findOrderDtoList();
        return orderDtoList;
    }


    @Data
    private class SimpleOrderResponse {
        private Long id;
        private String memberName;
        //private List<OrderItem> orderItems = new ArrayList<>();
        private Address address;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;

        public SimpleOrderResponse(Order order) {
            this.id = order.getId();
            this.memberName = order.getMember().getName();
            this.address = order.getDelivery().getAddress();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
        }
    }

    /**
     * V1 : 엔티티 직접 노출
     * 문제점 : 지연로딩 엔티티 강제로딩 필요, 양방향 관계가 있다면 무한루프 => 다 해결하는 방법은 있지만, 위험하고 성능에도 좋지 않음
     */
    /*@GetMapping
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }*/

    /**
     * V2 - DTO로 응답하기
     * 개선된 점 : 엔티티를 직접 내보내지 않고, 별도의 response DTO를 생성해서 필요한 값만 API스펙에 맞게 내보낸다.
     * 여전히 있는 문제점 : N + 1 문제. 처음 조회된 Order 갯수만큼 member, delivery 조회 쿼리가 발생한다.(주문이 100개면 member 100개, delivery 100개 => 총 201개(1 + N + N) 쿼리 발생)
     */
    /*@GetMapping
    public List<SimpleOrderResponse> ordersV2() {
        List<Order> orderEntities = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderResponse> response = orderEntities.stream()
            .map(order -> new SimpleOrderResponse(order))
            .toList();

        return response;
    }*/

    /**
     * V3 성능최적화 - N+1 문제 해결하기 > Fetch-Join
     */
    /*@GetMapping
    public List<SimpleOrderResponse> ordersV3() {
        List<Order> orderEntities = orderRepository.findAllWithMemberAndDelivery();
        List<SimpleOrderResponse> response = orderEntities.stream()
            .map(order -> new SimpleOrderResponse(order))
            .toList();
        return response;
    }*/
}
