package com.lingchen.buynow.service.user;

import com.lingchen.buynow.dto.UserDto;
import com.lingchen.buynow.entity.User;
import com.lingchen.buynow.request.AddUserRequest;
import com.lingchen.buynow.request.CreateUserRequest;
import com.lingchen.buynow.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    User addUser(AddUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    User getAuthenticatedUser();
    UserDto convertToDto(User user);
}
