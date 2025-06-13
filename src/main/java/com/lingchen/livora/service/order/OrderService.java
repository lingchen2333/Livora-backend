package com.lingchen.livora.service.order;

import com.lingchen.livora.dto.OrderDto;
import com.lingchen.livora.entity.*;
import com.lingchen.livora.enums.OrderStatus;
import com.lingchen.livora.repository.OrderRepository;
import com.lingchen.livora.request.PaymentRequest;
import com.lingchen.livora.service.cart.CartService;
import com.lingchen.livora.service.user.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
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
        return orderRepository.save(this.createOrder(userId));
    }

    private Order createOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new EntityNotFoundException("Cannot place order on an empty cart");
        }
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
    public String createPaymentIntent(PaymentRequest request) throws StripeException {
        long amountInSmallestUnit = Math.round(request.getAmount() * 100);
        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInSmallestUnit)
                        .setCurrency(request.getCurrency())
                        .addPaymentMethodType("card")
                        .build()
        );

        return intent.getClientSecret();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        order.getItems().size();
        return modelMapper.map(order, OrderDto.class);
    }
}
