package com.sheen.joe.bankingsystem.exception.handler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record HandlerResponse(
        String errorMessage,
        HttpStatus status,
        LocalDateTime timestamp
) {}
