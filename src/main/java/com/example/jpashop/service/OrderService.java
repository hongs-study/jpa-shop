package com.example.jpashop.service;

import com.example.jpashop.domain.entity.Delivery;
import com.example.jpashop.domain.entity.Member;
import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.domain.entity.OrderItem;
import com.example.jpashop.domain.entity.item.Item;
import com.example.jpashop.repository.ItemRepository;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId);
        Item item = itemRepository.findById(itemId);

        // 신규 엔티티 -> persist() 필요하다
        Delivery delivery = Delivery.createDelivery(member.getAddress());
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // Order 만 저장하면 delivery, orderItem 자동저장 된다 => casecade 가 적혀있기 때문!
        Order order = Order.createOrder(member, delivery, orderItem);

        Long orderId = orderRepository.save(order);
        return orderId;
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findById(orderId);

        // 주문 취소 로직 호출
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
