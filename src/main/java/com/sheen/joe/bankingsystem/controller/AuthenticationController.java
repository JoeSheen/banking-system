package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;
import com.sheen.joe.bankingsystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.registerUser(requestDto);
        log.info("User with ID: {} registered", authenticationResponseDto.id());
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.loginUser(loginRequestDto);
        log.info("User with ID: {} logged in", authenticationResponseDto.id());
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.OK);
    }

}
