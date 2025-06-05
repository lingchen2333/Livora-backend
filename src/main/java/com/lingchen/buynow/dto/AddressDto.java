package com.lingchen.buynow.dto;

import com.lingchen.buynow.enums.AddressType;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String country;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String postCode;
    private String phone;
    private AddressType addressType;
}
