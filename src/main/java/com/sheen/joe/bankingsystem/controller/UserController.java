package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;
import com.sheen.joe.bankingsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> updateUsername(@PathVariable("userId") UUID id) {
        UserResponseDto userResponseDto = userService.generateNewUsername(id);
        log.info("Username: {} updated for user (ID): {}", userResponseDto.username(), userResponseDto.id());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> userResponseDtoList = userService.getAllUsers();
        log.info("Found {} users", userResponseDtoList.size());
        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> getById(@PathVariable("userId") UUID id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        log.info("User with ID: {} found", userResponseDto.id());
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

}
