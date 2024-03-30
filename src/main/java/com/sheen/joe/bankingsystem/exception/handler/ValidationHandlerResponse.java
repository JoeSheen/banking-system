package com.sheen.joe.bankingsystem.exception.handler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationHandlerResponse(
        Map<String, String> errors,
        HttpStatus status,
        LocalDateTime timestamp
) {}
