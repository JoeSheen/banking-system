package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;

import java.util.UUID;

public interface UserService {

    UserResponseDto generateNewUsername(UUID id);

    UserResponseDto getUserById(UUID id);

    CollectionResponseDto<UserResponseDto> getAllUsers(int pageNumber, int pageSize);

}
