package com.lingchen.buynow.service.address;


import com.lingchen.buynow.dto.AddressDto;
import com.lingchen.buynow.entity.Address;
import com.lingchen.buynow.entity.User;
import com.lingchen.buynow.repository.AddressRepository;
import com.lingchen.buynow.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final IUserService userService;

    @Override
    public List<Address> getAddressListByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address addAddressForUser(Address address, Long userId) {
        User user = userService.getUserById(userId);
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public void deleteAddressById(Long addressId) {
        Address address = this.getAddressById(addressId);
        addressRepository.delete(address);
    }

    @Override
    public Address updateAddressById(Long addressId, Address address) {
        Address oldAddress = this.getAddressById(addressId);
        oldAddress.setCountry(address.getCountry());
        oldAddress.setCity(address.getCity());
        oldAddress.setAddressLine1(address.getAddressLine1());
        oldAddress.setAddressLine2(address.getAddressLine2());
        oldAddress.setPostCode(address.getPostCode());
        oldAddress.setPhone(address.getPhone());
        oldAddress.setAddressType(address.getAddressType());
        return addressRepository.save(oldAddress);
    }

    @Override
    public AddressDto convertToDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }


}
