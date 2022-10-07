package com.example.jpashop.repository.order.query;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderQueryDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String memberName, LocalDateTime orderDate,
        OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        //this.orderItems = orderItems;
    }
}
