package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.UserMapper;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.service.UserService;
import com.sheen.joe.bankingsystem.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto generateNewUsername(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with ID: %s not found", id)));
        String username = user.getFirstName() + user.getLastName() + StringUtils.generateRandomAlphanumeric(6, true);
        user.setUsername(username);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with ID: %s not found", id)));
        return userMapper.toUserResponse(user);
    }

    @Override
    public CollectionResponseDto<UserResponseDto> getAllUsers(int pageNumber, int pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<UserResponseDto> page = userRepository.findAll(paging).map(userMapper::toUserResponse);
        return new CollectionResponseDto<>(page.getContent(), page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSort().isSorted());
    }
}
