package com.lingchen.livora.repository;

import com.lingchen.livora.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  Cart findByUserId(Long userId);
}
