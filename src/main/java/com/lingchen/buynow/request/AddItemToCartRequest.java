package com.lingchen.buynow.request;

import lombok.Data;

@Data
public class AddItemToCartRequest {
    private Long productId;
    private int quantity;
}
