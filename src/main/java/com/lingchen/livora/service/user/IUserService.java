package com.lingchen.livora.service.user;

import com.lingchen.livora.dto.UserDto;
import com.lingchen.livora.entity.User;
import com.lingchen.livora.request.AddUserRequest;
import com.lingchen.livora.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long userId);
    User addUser(AddUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    User getAuthenticatedUser();
    UserDto convertToDto(User user);
}
