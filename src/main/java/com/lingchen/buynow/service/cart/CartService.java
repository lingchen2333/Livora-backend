package com.lingchen.buynow.service.cart;

import com.lingchen.buynow.dto.CartDto;
import com.lingchen.buynow.entity.Cart;
import com.lingchen.buynow.entity.User;
import com.lingchen.buynow.repository.CartItemRepository;
import com.lingchen.buynow.repository.CartRepository;
import com.lingchen.buynow.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find cart with id: " + id));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        //check the user is present
        //userService.getUserById(userId);
        return cartRepository.findByUserId(userId);
    }

    @Override
    public void deleteCart(Long cartId) {
        Cart cart = this.getCartById(cartId);
        cart.removeAllItems();
        cartRepository.deleteById(cartId);
        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    public Cart initialiseNewCartForUser(User user) {
//        Cart cart = cartRepository.findByUserId(userId);
//        if (cart == null) {
//            cart = new Cart();
//            cart.setUser(userService.getUserById(userId));
//            return cartRepository.save(cart);
//        }
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal getCartTotalAmount(Long cartId) {
        Cart cart = this.getCartById(cartId);
        return cart.getTotalAmount();
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        cart.getItems().size();
        return modelMapper.map(cart, CartDto.class);
    }
}
