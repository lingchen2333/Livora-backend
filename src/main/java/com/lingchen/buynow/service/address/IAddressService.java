package com.lingchen.buynow.service.address;

import com.lingchen.buynow.dto.AddressDto;
import com.lingchen.buynow.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> getAddressListByUserId(Long userId);
    List<Address> createAddressList(List<Address> addressList);
    Address getAddressById(Long addressId);
    void deleteAddressById(Long addressId);
    Address updateAddressById(Long addressId, Address address);

    AddressDto convertToDto(Address address);
}
