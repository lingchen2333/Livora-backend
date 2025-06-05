package com.lingchen.livora.controller;

import com.lingchen.livora.dto.UserDto;
import com.lingchen.livora.entity.User;
import com.lingchen.livora.request.AddUserRequest;
import com.lingchen.livora.request.UpdateUserRequest;
import com.lingchen.livora.response.ApiResponse;
import com.lingchen.livora.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User get successfully", userDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addUser(@RequestBody AddUserRequest request) {
        User user = userService.addUser(request);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User added successfully", userDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
        User user = userService.updateUser(request, userId);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User updated successfully", userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
    }
}
