package com.lingchen.livora.service.user;

import com.lingchen.livora.dto.UserDto;
import com.lingchen.livora.entity.Role;
import com.lingchen.livora.entity.User;
import com.lingchen.livora.repository.AddressRepository;
import com.lingchen.livora.repository.RoleRepository;
import com.lingchen.livora.repository.UserRepository;
import com.lingchen.livora.request.AddUserRequest;
import com.lingchen.livora.request.UpdateUserRequest;
import com.lingchen.livora.service.cart.ICartService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final ICartService cartService;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + userId + " not found"));
    }

    @Override
    public User addUser(AddUserRequest request) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("role with name ROLE_USER not found"));

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException(request.getEmail() + " already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);

        //save address
        request.getAddressList()
                .forEach(address -> {
                    address.setUser(savedUser);
                    addressRepository.save(address);
                });

        //initialise new cart for user
        cartService.initialiseNewCartForUser(savedUser);
        return savedUser;
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        User user = this.getUserById(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = this.getUserById(userId);
        //User & Role
        user.getRoles().forEach(role -> {
            role.removeUser(user);
        });
        userRepository.delete(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Log in required!"));
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
