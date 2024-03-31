package com.sheen.joe.bankingsystem.dto;

import com.sheen.joe.bankingsystem.annotation.ValidPassword;

import java.time.LocalDate;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        @ValidPassword
        String password
) {}
