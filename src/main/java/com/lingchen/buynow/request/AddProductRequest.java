package com.lingchen.buynow.request;

import lombok.Data;

import java.math.BigDecimal;

import com.lingchen.buynow.entity.Category;


@Data
public class AddProductRequest {

    //private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
