package com.example.jpashop.repository.order.simplequery;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderSimpleQueryDto {
    private Long id;
    private String memberName;
    private Address address;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    public OrderSimpleQueryDto(Long id, String memberName, Address address, LocalDateTime orderDate,
        OrderStatus orderStatus) {
        this.id = id;
        this.memberName = memberName;
        this.address = address;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
}
