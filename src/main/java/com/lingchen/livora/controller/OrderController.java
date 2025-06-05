package com.lingchen.livora.controller;


import com.lingchen.livora.dto.OrderDto;
import com.lingchen.livora.entity.Order;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;


    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderDto> orderDtos = orders
                .stream()
                .map(orderService::convertToDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse("Found all orders successfully", orderDtos));

    }


    @PostMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId) {
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully", orderDto));
    }
}
