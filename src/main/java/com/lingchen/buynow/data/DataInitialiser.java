package com.lingchen.buynow.data;

import com.lingchen.buynow.entity.Role;
import com.lingchen.buynow.entity.User;
import com.lingchen.buynow.repository.RoleRepository;
import com.lingchen.buynow.repository.UserRepository;
import com.lingchen.buynow.service.cart.ICartItemService;
import com.lingchen.buynow.service.cart.ICartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Transactional
@Component
@RequiredArgsConstructor
public class DataInitialiser implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICartService cartService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        this.createRole(defaultRoles);
        this.createDefaultAdmin();
    }

    private void createRole(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .forEach(role -> roleRepository.save(new Role(role)));
    }


    private void createDefaultAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new EntityNotFoundException("Admin role not found"));
        for (int i = 1; i < 4; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setLastName("admin");
            user.setFirstName("admin");
            user.setRoles(Set.of(adminRole));
            User savedUser = userRepository.save(user);
            //initialise cart
            cartService.initialiseNewCartForUser(savedUser);
        }
    }
}
