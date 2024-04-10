package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.User;

public interface AuthenticationMapper {

    User toUser(RegisterRequestDto registerRequestDto);

    AuthenticationResponseDto toAuthenticationResponse(User user, String token);

}
