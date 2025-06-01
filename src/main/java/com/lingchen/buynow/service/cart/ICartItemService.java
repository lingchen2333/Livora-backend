package com.lingchen.buynow.service.cart;

import com.lingchen.buynow.dto.CartItemDto;
import com.lingchen.buynow.entity.Cart;
import com.lingchen.buynow.entity.CartItem;
import com.lingchen.buynow.entity.Product;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long itemId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);

    CartItemDto convertToDto(CartItem cartItem);
}
