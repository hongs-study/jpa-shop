package com.example.jpashop;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.entity.Delivery;
import com.example.jpashop.domain.entity.Member;
import com.example.jpashop.domain.entity.Order;
import com.example.jpashop.domain.entity.OrderItem;
import com.example.jpashop.domain.entity.item.Book;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = getMember("userA");
            em.persist(member);

            Book book1 = getBook("선녀와 나무꾼", 10000);
            em.persist(book1);
            Book book2 = getBook("개미", 20000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Order order = Order.createOrder(member, Delivery.createDelivery(member.getAddress()),
                orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = getMember("userB");
            em.persist(member);

            Book book1 = getBook("Java 전공책", 20000);
            em.persist(book1);
            Book book2 = getBook("컴퓨터공학", 40000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Order order = Order.createOrder(member, Delivery.createDelivery(member.getAddress()),
                orderItem1, orderItem2);
            em.persist(order);
        }


        private static Member getMember(String memberName) {
            Member member = new Member();
            member.setName(memberName);
            member.setAddress(new Address("서울시", "강남구 1", "11111"));
            return member;
        }

        private static Book getBook(String bookName, int price) {
            Book book1 = new Book();
            book1.setName(bookName);
            book1.setPrice(price);
            book1.setStockQuantity(100);
            return book1;
        }
    }

}
