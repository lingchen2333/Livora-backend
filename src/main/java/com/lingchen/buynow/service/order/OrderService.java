package com.lingchen.buynow.service.order;

import com.lingchen.buynow.dto.OrderDto;
import com.lingchen.buynow.entity.*;
import com.lingchen.buynow.enums.OrderStatus;
import com.lingchen.buynow.repository.OrderRepository;
import com.lingchen.buynow.service.cart.CartService;
import com.lingchen.buynow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {

    private final CartService cartService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Order order = orderRepository.save(this.createOrder(userId));
        return order;
    }

    private Order createOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        User user = userService.getUserById(userId);
        //create order from cart
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalAmount());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setItems(this.getOrderItemsFromCart(cart, order));
        //delete the cart
        cartService.deleteCart(cart.getId());
        return order;
    }

    private HashSet<OrderItem> getOrderItemsFromCart(Cart cart, Order order) {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    //update product inventory
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    return new OrderItem(
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice(),
                            cartItem.getProduct(),
                            order);
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
