package com.lingchen.livora.service.address;

import com.lingchen.livora.dto.AddressDto;
import com.lingchen.livora.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> getAddressListByUserId(Long userId);
    Address addAddressForUser(Address address, Long userId);
    Address getAddressById(Long addressId);
    void deleteAddressById(Long addressId);
    Address updateAddressById(Long addressId, Address address);

    AddressDto convertToDto(Address address);
}
