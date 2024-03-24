package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toUserResponse(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(),
                user.getDateOfBirth(), user.getPhoneNumber(), user.getEmail(), user.getUsername());
    }

}
