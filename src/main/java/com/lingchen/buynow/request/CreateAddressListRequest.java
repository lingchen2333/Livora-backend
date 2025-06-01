package com.lingchen.buynow.request;

import com.lingchen.buynow.entity.Address;
import lombok.Data;

import java.util.List;

@Data
public class CreateAddressListRequest {
    private List<Address> addressList;

}
