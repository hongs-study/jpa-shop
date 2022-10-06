package com.example.jpashop.api;

import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/simple-orders")
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * V1 : 엔티티 직접 노출
     * 문제점 : 지연로딩 엔티티 강제로딩 필요, 양방향 관계가 있다면 무한루프 => 다 해결하는 방법은 있지만, 위험하고 성능에도 좋지 않음
     */
    @GetMapping
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

}
