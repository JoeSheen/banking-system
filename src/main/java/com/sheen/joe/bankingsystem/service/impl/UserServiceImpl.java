package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.UserMapper;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with ID: %s not found", id)));
        return userMapper.toUserResponse(user);
    }
}
