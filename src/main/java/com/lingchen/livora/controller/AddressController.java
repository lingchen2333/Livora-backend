package com.lingchen.livora.controller;


import com.lingchen.livora.dto.AddressDto;
import com.lingchen.livora.entity.Address;
import com.lingchen.livora.request.AddAddressRequest;
import com.lingchen.livora.request.UpdateAddressRequest;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.address.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}")
public class AddressController {

    private final IAddressService addressService;

    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse> findAddressListByUserId(@PathVariable Long userId) {
        List<Address> addressList = addressService.getAddressListByUserId(userId);
        List<AddressDto> addressDtoList = addressList.stream()
                .map(addressService::convertToDto)
                .toList();
        return ResponseEntity.ok(new ApiResponse("Address list found for user with id " + userId, addressDtoList));
    }

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<ApiResponse> addAddressForUser(@RequestBody AddAddressRequest request, @PathVariable Long userId) {
        AddressDto addressDto = addressService.convertToDto(addressService.addAddressForUser(request.getAddress(), userId));
        return ResponseEntity.ok(new ApiResponse("Address added successfully", addressDto));
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        AddressDto addressDto = addressService.convertToDto(address);
        return ResponseEntity.ok(new ApiResponse("Address found with id " + addressId, addressDto));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddressById(@PathVariable Long addressId) {
        addressService.deleteAddressById(addressId);
        return ResponseEntity.ok(new ApiResponse("Address with id " + addressId + " deleted successfully", null));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse> updateAddressById(@PathVariable Long addressId, @RequestBody UpdateAddressRequest request) {
        addressService.updateAddressById(addressId, request.getAddress());
        return ResponseEntity.ok(new ApiResponse("Address updated successfully", null));
    }
}
