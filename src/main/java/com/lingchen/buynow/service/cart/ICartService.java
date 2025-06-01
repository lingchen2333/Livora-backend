package com.lingchen.buynow.service.cart;

import com.lingchen.buynow.dto.CartDto;
import com.lingchen.buynow.dto.UserDto;
import com.lingchen.buynow.entity.Cart;
import com.lingchen.buynow.entity.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCartById(Long id);

    Cart getCartByUserId(Long userId);

    void deleteCart(Long cartId);

    Cart initialiseNewCartForUser(User user);

    BigDecimal getCartTotalAmount(Long cartId);

    CartDto convertToDto(Cart cart);
}
