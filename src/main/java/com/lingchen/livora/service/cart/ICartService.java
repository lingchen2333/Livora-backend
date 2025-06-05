package com.lingchen.livora.service.cart;

import com.lingchen.livora.dto.CartDto;
import com.lingchen.livora.entity.Cart;
import com.lingchen.livora.entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCartById(Long id);

    Cart getCartByUserId(Long userId);

    void deleteCart(Long cartId);

    Cart initialiseNewCartForUser(User user);

    BigDecimal getCartTotalAmount(Long cartId);

    CartDto convertToDto(Cart cart);
}
