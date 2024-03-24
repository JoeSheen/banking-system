package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;

import java.util.UUID;

public interface UserService {

    UserResponseDto getUserById(UUID id);

}
