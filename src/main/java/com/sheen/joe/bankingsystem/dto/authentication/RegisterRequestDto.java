package com.sheen.joe.bankingsystem.dto.authentication;

import com.sheen.joe.bankingsystem.annotation.ValidPassword;
import com.sheen.joe.bankingsystem.entity.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        @NotNull
        Country country,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email,
        @ValidPassword
        String password
) {}
