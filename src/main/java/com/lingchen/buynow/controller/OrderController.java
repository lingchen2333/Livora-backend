package com.lingchen.buynow.controller;


import com.lingchen.buynow.dto.OrderDto;
import com.lingchen.buynow.entity.Order;
import com.lingchen.buynow.response.ApiResponse;
import com.lingchen.buynow.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
