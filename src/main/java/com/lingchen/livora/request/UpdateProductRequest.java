package com.lingchen.livora.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private String categoryName;
}
