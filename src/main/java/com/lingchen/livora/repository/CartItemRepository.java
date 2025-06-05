package com.lingchen.livora.repository;

import com.lingchen.livora.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByProductId(Long productId);

    void deleteByCartId(Long cartId);

}