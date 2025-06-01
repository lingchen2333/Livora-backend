package com.lingchen.buynow.controller;


import com.lingchen.buynow.dto.CartDto;
import com.lingchen.buynow.entity.Cart;
import com.lingchen.buynow.response.ApiResponse;
import com.lingchen.buynow.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> findCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        CartDto cartDto = cartService.convertToDto(cart);
        return ResponseEntity.ok(new ApiResponse("Cart found for user with id " + userId, cartDto));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Cart with id " + cartId + " deleted successfully", null));
    }
}
