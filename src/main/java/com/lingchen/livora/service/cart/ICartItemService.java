package com.lingchen.livora.service.cart;

import com.lingchen.livora.dto.CartItemDto;
import com.lingchen.livora.entity.CartItem;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long itemId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);

    CartItemDto convertToDto(CartItem cartItem);
}
