package com.example.jpashop.repository.order.query;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderFlatDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    //private List<OrderItemQueryDto> orderItems;
    private String itemName;
    private int orderPrice;
    private int orderCount;

    public OrderFlatDto(Long orderId, String memberName, LocalDateTime orderDate,
        OrderStatus orderStatus, Address address, String itemName, int orderPrice, int orderCount) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }
}
