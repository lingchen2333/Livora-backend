package com.lingchen.livora.controller;


import com.lingchen.livora.dto.OrderDto;
import com.lingchen.livora.entity.Order;
import com.lingchen.livora.request.PaymentRequest;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.order.IOrderService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class OrderController {

    private final IOrderService orderService;


    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderDto> orderDtos = orders
                .stream()
                .map(orderService::convertToDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse("Found all orders successfully", orderDtos));

    }


    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        String clientSecret = orderService.createPaymentIntent(request);
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
