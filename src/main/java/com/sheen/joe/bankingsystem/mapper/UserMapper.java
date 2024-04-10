package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.User;

public interface UserMapper {

    UserResponseDto toUserResponse(User user);

}
