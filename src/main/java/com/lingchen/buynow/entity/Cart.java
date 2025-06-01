package com.lingchen.buynow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();


    public void removeItem(CartItem cartItem) {
        items.remove(cartItem);
        cartItem.setCart(null);
        this.updateCartTotalAmount();
    }

    public void updateCartTotalAmount() {
        BigDecimal newTotalAmount = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.setTotalAmount(newTotalAmount);
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        this.updateCartTotalAmount();
    }

    public void removeAllItems() {
        items.forEach(item -> item.setCart(null));
        items.clear();
        this.updateCartTotalAmount();
    }
}
