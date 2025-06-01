package com.lingchen.buynow.request;

import com.lingchen.buynow.entity.Address;
import lombok.Data;

import java.util.List;

@Data
public class AddUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private List<Address> addressList;
}
