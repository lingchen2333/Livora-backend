package com.lingchen.livora.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id") //name of the column is category_id
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //product attribute in Image
    private List<Image> images;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;


    @OneToMany(mappedBy = "product") //order item without any product continue to exist in order
    private List<OrderItem> orderItems;

    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }
}
