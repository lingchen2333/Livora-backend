package com.lingchen.buynow.repository;

import com.lingchen.buynow.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findOrdersByUserId(Long userId);
}