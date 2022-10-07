package com.example.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.domain.entity.OrderItem;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.order.query.OrderFlatDto;
import com.example.jpashop.repository.order.query.OrderItemQueryDto;
import com.example.jpashop.repository.order.query.OrderQueryDto;
import com.example.jpashop.repository.order.query.OrderQueryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v4/orders")
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/6")
    public List<OrderQueryDto> orderV6() {
        List<OrderFlatDto> flatdata = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> response = flatdata.stream()
            .collect(
                groupingBy(
                    o -> new OrderQueryDto(o.getOrderId(), o.getMemberName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                    mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getOrderCount()), toList())
            ))
            .entrySet().stream()
            .map(o -> new OrderQueryDto(o.getKey().getOrderId(), o.getKey().getMemberName()
                , o.getKey().getOrderDate(), o.getKey().getOrderStatus(), o.getKey().getAddress()
                , o.getValue()))
            .toList();
        return response;
    }

    /**
     * 컬렉션조회V5 - N+1문제 해결하기
     * 결과 : 쿼리 2번만 실행됨
     * 방법 : 메인쿼리 조회 -> ID추출 -> In쿼리로 서브데이터 조회 -> 메모리에서 메인데이터-서브데이터 합침
     *      for문이 늘어나서 더 문제가 되지 않나 싶었다. 하지만 Java-GroupBy로 만들었기 때문에 성능은 오히려 향상된다. -> 복잡도를 보면 O(1)이다
     */
    @GetMapping("5")
    public List<OrderQueryDto> ordersV5() {
        List<OrderQueryDto> orders = orderQueryRepository.findAllByDto_optimization();
        return orders;
    }

    /**
     *  ToMany 관계(컬렉션조회) V3.1 (엔티티)- 페이징 한계 돌파
     * 방법 : default_batch_fetch_size, @BatchSize
     */
    @GetMapping("/3.1")
    public List<OrderDto> ordersV3_1_page(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        List<Order> orders = orderRepository.findAllWithItemPaging(offset, pageSize);
        List<OrderDto> result = orders.stream()
            .map(o -> new OrderDto(o))
            .toList();
        return result;
    }

    /**
     * ToMany 관계(컬렉션조회) v4 - DTO 직접 조회하기
     * 방법: 메인 쿼리 조회 후 결과 개수만큼 for 돌면서 컬렉션 조회한다
     * 문제점: 결국 N+1문제 발생(100건 조회 -> 101개 쿼리 실행됨)
     */
    /*@GetMapping("4")
    public List<OrderQueryDto> orders4() {
        List<OrderQueryDto> orders = orderQueryRepository.findAllByDto();
        return orders;
    }*/

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .toList();
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int orderCount;
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            orderCount = orderItem.getCount();
        }
    }

    /**
     * 고급조회. 컬렉션 조회 최적화 V3 - DTO변환 + 페치조인 + distinct
     * 컬렉션 조회시 문제점 : 데이터 뻥튀기 되는 문제
     * 최적화 방법
     *  1. 페치조인
     *  2. distinct (JPA의 distinct : SQL의 distinct 기능 + 엔티티 중복시 컬렉션에 담아준다)
     *
     * but, 단점 & 주의사항
     *  1. 페이징 할 수 없다(모든 데이터 조회해서 메모리에서 처리함) => 즉, 페이징 안쓰면 써도 됨.
     *  2. 컬렉션 페치조인은 일대다 관계 1개만 가능하다. (뻥뻥튀기되니까)
     */
    /*@GetMapping
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
            .map(o -> new OrderDto(o))
            .toList();
        return result;
    }*/
}
