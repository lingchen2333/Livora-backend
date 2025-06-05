package com.lingchen.livora.request;

import lombok.Data;

@Data
public class AddItemToCartRequest {
    private Long productId;
    private int quantity;
}
