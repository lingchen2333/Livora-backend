package com.lingchen.buynow.repository;

import com.lingchen.buynow.entity.Address;
import com.lingchen.buynow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> user(User user);

  List<Address> findByUserId(Long userId);
}