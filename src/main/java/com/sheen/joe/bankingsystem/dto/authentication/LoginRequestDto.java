package com.sheen.joe.bankingsystem.dto.authentication;

public record LoginRequestDto(
        String username,
        String password
) {}
