package com.lingchen.buynow.service.order;

import com.lingchen.buynow.dto.OrderDto;
import com.lingchen.buynow.entity.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    List<Order> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
