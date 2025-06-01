package com.lingchen.buynow.controller;


import com.lingchen.buynow.dto.CartItemDto;
import com.lingchen.buynow.entity.Cart;
import com.lingchen.buynow.entity.CartItem;
import com.lingchen.buynow.entity.User;
import com.lingchen.buynow.request.AddItemToCartRequest;
import com.lingchen.buynow.request.UpdateItemQuantityRequest;
import com.lingchen.buynow.response.ApiResponse;
import com.lingchen.buynow.service.cart.ICartItemService;
import com.lingchen.buynow.service.cart.ICartService;
import com.lingchen.buynow.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemToCartRequest request) {
        User user = userService.getAuthenticatedUser();
        //Cart cart = cartService.initialiseNewCartForUser(user.getId());
        Cart cart = cartService.getCartByUserId(user.getId());
        CartItem item = cartItemService.addItemToCart(cart.getId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new ApiResponse("Item added successfully", this.cartItemService.convertToDto(item)));
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @RequestParam Long productId) {
        cartItemService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok(
                new ApiResponse("Item with product id " + productId + " deleted successfully from cart with id " + cartId, null));
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(
            @PathVariable Long cartId, @PathVariable Long itemId, @RequestBody UpdateItemQuantityRequest request) {
        cartItemService.updateItemQuantity(cartId, itemId, request.getQuantity());
        return ResponseEntity.ok(
                new ApiResponse("Item updated successfully", null));
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse> getCartItem(@PathVariable Long cartId, @RequestParam Long productId) {
        CartItem cartItem = cartItemService.getCartItem(cartId, productId);
        CartItemDto cartItemDto = cartItemService.convertToDto(cartItem);
        return ResponseEntity.ok(new ApiResponse("Found item with product id " + productId, cartItemDto));
    }

}
