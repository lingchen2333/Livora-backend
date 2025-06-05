package com.lingchen.livora.service.cart;

import com.lingchen.livora.dto.CartItemDto;
import com.lingchen.livora.entity.Cart;
import com.lingchen.livora.entity.CartItem;
import com.lingchen.livora.entity.Product;
import com.lingchen.livora.repository.CartItemRepository;
import com.lingchen.livora.repository.CartRepository;
import com.lingchen.livora.service.product.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ICartService cartService;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public CartItem addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        Product product = productService.getProductById(productId);
        //if the product is already a cart item
        Optional<CartItem> cartItemOptional = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        CartItem item;
        if (cartItemOptional.isPresent()) { //product is already a cart item
            item = cartItemOptional.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setUnitPrice(product.getPrice());
            item.updateTotalPrice();
            cart.updateCartTotalAmount();
        } else {
            item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
            item.setCart(cart);
            item.updateTotalPrice();
            cart.addItem(item);
        }
        cartItemRepository.save(item);
        cartRepository.save(cart);
        return item;
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCartById(cartId);
        CartItem item = this.getCartItem(cartId, productId);
        cart.removeItem(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long itemId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item " + itemId + " not found in cart " + cartId));
        //Product product = productService.getProductById(productId);
        Product product = productService.getProductById(item.getProduct().getId());
        //CartItem item = this.getCartItem(cartId, productId);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.updateTotalPrice();
        cart.updateCartTotalAmount();

        cartRepository.save(cart);
        cartItemRepository.save(item);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCartById(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("product with id " + productId + " not found in the cart with id " + cartId));
    }

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
