package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.authentication.*;
import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final JwtUtils jwtUtils;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.registerUser(requestDto);
        log.info("User with ID: {} registered", authenticationResponseDto.id());
        String tokenCookieString = jwtUtils.generateTokenCookie(authenticationResponseDto.username());
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, tokenCookieString)
                .body(authenticationResponseDto);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.loginUser(loginRequestDto);
        log.info("User with ID: {} logged in", authenticationResponseDto.id());
        String tokenCookieString = jwtUtils.generateTokenCookie(authenticationResponseDto.username());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, tokenCookieString).body(authenticationResponseDto);
    }

}
