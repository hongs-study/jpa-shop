package com.example.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.jpashop.NotEnoughStockException;
import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.entity.Member;
import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.domain.entity.OrderItem;
import com.example.jpashop.domain.entity.item.Book;
import com.example.jpashop.domain.entity.item.Item;
import com.example.jpashop.repository.OrderRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("주문을 테스트한다")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @DisplayName("상품주문")
    @Test
    void createorder() {
        //given
        Member member = createMember();
        em.persist(member);// 간단하게 repository 보다 바로 em으로 호출함
        Item book = createItem(100);
        em.persist(book);

        int orderCount = 3;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        em.flush();
        em.clear();

        //then
        Order findOrder = orderRepository.findById(orderId);
        Item findItem = em.find(Item.class, book.getId());
        assertAll("주문 회원 데이터 검증"
            , () -> assertEquals(OrderStatus.ORDER, findOrder.getStatus(), "주문 후 상태는 ORDER")
            , () -> assertEquals(member.getId(), findOrder.getMember().getId(), "요청 회원ID과 주문의 회원ID는 동일해야한다.")
            , () -> assertEquals(member.getAddress(), findOrder.getDelivery().getAddress(), "요청 회원의 주소와 주문의 주소는 동일해야한다.")
        );
        assertAll("주문 데이터 검증"
            , () -> assertEquals(book.getId(), findOrder.getOrderItems().get(0).getItem().getId(), "주문 상품ID가 동일해야한다")
            , () -> assertEquals(book.getPrice() * orderCount, findOrder.getTotalPrice(), "주문가격은 가격 * 수량이다")
            , () -> assertEquals(1, findOrder.getOrderItems().size(), "주문 상품 종류 수가 같아야한다.")
            , () -> assertEquals(97, findItem.getStockQuantity(), "주문 후 재고가 줄어야 한다")
        );
    }

    @DisplayName("주문재고수량초과 에러")
    @Test
    void overStock() {
        //given
        Member member = createMember();
        em.persist(member);// 간단하게 repository 보다 바로 em으로 호출함
        Item book = createItem(100);
        em.persist(book);

        em.flush();
        em.clear();

        int orderCount = 103;

        //when & then
        NotEnoughStockException exception = assertThrows(
            NotEnoughStockException.class, () -> {
                orderService.order(member.getId(), book.getId(), orderCount);
            }, "!!테스트 실패!! 주문수량초과 에러가 발생해야한다");
        assertEquals("재고가 없습니다.", exception.getMessage());
    }

    @DisplayName("주문취소")
    @Test
    void cancel() {
        //given
        Member member = createMember();
        em.persist(member);
        int stockQuantity = 100;
        Item book = createItem(stockQuantity);
        em.persist(book);

        Long orderId = orderService.order(member.getId(), member.getId(), 3);
        em.flush();
        em.clear();

        //when
        orderService.cancelOrder(orderId);
        em.flush();
        em.clear();

        //then
        Order findOrder = em.find(Order.class, orderId);
        OrderItem orderItem = findOrder.getOrderItems().get(0);
        assertAll("주문취소검증"
            , () -> assertEquals(OrderStatus.CANCEL, findOrder.getStatus(), "주문상태는 CANCEL 이여야 한다.")
            , () -> assertEquals(stockQuantity, orderItem.getItem().getStockQuantity(), "재고가 복구되어야 한다.")
        );
    }

    private static Item createItem(int stockQuantity) {
        Item book = new Book();
        book.setName("책상품");
        book.setStockQuantity(stockQuantity);
        book.setPrice(10000);
        return book;
    }

    private static Member createMember() {
        Member member = new Member("고객1");
        member.setAddress(new Address("서울시", "강남구 1로 123", "12345"));
        return member;
    }
}