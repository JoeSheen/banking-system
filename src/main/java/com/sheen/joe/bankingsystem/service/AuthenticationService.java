package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;

public interface AuthenticationService {

    AuthenticationResponseDto registerUser(RegisterRequestDto registerRequestDto);

    AuthenticationResponseDto loginUser(LoginRequestDto loginRequestDto);

}
