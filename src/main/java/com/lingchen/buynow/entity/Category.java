package com.lingchen.buynow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore //stop Category from calling product
    @OneToMany(mappedBy = "category") //category attribute in Product
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
