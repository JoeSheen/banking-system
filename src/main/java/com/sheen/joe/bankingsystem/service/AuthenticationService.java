package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.RegisterRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto registerUser(RegisterRequestDto registerRequestDto);

    AuthenticationResponseDto loginUser(LoginRequestDto loginRequestDto);

}
