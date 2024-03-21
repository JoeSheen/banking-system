package com.sheen.joe.bankingsystem.dto;

import java.time.LocalDate;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber,
        String email,
        String password
) {}
