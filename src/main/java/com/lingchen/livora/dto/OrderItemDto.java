package com.lingchen.livora.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String itemName;
    private String itemBrand;
    private Long itemProductId;
}
