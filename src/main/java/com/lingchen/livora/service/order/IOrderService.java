package com.lingchen.livora.service.order;

import com.lingchen.livora.dto.OrderDto;
import com.lingchen.livora.entity.Order;
import com.lingchen.livora.request.PaymentRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    List<Order> getUserOrders(Long userId);

    String createPaymentIntent(PaymentRequest request) throws StripeException;

    OrderDto convertToDto(Order order);
}
