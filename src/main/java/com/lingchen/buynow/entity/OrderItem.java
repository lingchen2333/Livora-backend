package com.lingchen.buynow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private String itemName;
    private String itemBrand;
    private Long itemProductId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(int quantity, BigDecimal unitPrice, Product product, Order order) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;

        this.itemName = product.getName();
        this.itemBrand = product.getBrand();
        this.itemProductId = product.getId();

        this.product = product;
        this.order = order;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal updateTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
