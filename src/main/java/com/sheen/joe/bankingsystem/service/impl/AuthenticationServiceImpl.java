package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AuthenticationMapper;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final AuthenticationMapper mapper;

    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        if (validateRegistrationRequest(registerRequestDto.phoneNumber(), registerRequestDto.email())) {
            throw new InvalidRequestException("Phone number or email already registered");
        }
        User user = userRepository.save(mapper.toUser(registerRequestDto));
        String token = jwtUtils.generateToken(user.getUsername());
        return mapper.toAuthenticationResponse(user, token);
    }

    @Override
    public AuthenticationResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password()));
        UUID userId = ((SecurityUser) authentication.getPrincipal()).getId();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with ID: %s not found", userId)));
        String token = jwtUtils.generateToken(user.getUsername());
        return mapper.toAuthenticationResponse(user, token);
    }

    private boolean validateRegistrationRequest(String phoneNumber, String email) {
        return userRepository.existsByPhoneNumber(phoneNumber) || userRepository.existsByEmail(email);
    }

}
