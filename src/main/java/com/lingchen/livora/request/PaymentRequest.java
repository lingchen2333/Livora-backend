package com.lingchen.livora.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private int amount;
    private String currency;
}
