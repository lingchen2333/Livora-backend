package com.lingchen.livora.repository;

import com.lingchen.livora.entity.Address;
import com.lingchen.livora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> user(User user);

  List<Address> findByUserId(Long userId);
}