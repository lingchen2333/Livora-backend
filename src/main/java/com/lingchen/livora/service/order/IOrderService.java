package com.lingchen.livora.service.order;

import com.lingchen.livora.dto.OrderDto;
import com.lingchen.livora.entity.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    List<Order> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
