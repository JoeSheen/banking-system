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
        // String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        // ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        // MultiValueMap<String, String> headers
        // MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        // headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        // return new ResponseEntity<>(authenticationResponseDto, headers, HttpStatus.CREATED);
        //https://stackoverflow.com/questions/76314410/spring-boot-creates-new-jsessionid-on-each-request
        //https://www.bezkoder.com/spring-boot-login-example-mysql/
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthenticationResponseDto authenticationResponseDto = authenticationService.loginUser(loginRequestDto);
        log.info("User with ID: {} logged in", authenticationResponseDto.id());
        return new ResponseEntity<>(authenticationResponseDto, HttpStatus.OK);
    }

}
