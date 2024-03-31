package com.sheen.joe.bankingsystem.exception.handler;

import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ResponseEntity<HandlerResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        LocalDateTime timestamp = LocalDateTime.now(ZONE_ID);
        HandlerResponse response = new HandlerResponse(exception.getMessage(), notFound, timestamp);
        log.info("Error: '{}' at: '{}'", response.errorMessage(), response.timestamp());
        return new ResponseEntity<>(response, notFound);
    }

    @ExceptionHandler(value = { InvalidRequestException.class })
    public ResponseEntity<HandlerResponse> handleInvalidRequestException(InvalidRequestException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        LocalDateTime timestamp = LocalDateTime.now(ZONE_ID);
        HandlerResponse response = new HandlerResponse(exception.getMessage(), badRequest, timestamp);
        log.info("Error: '{}' at: '{}'", response.errorMessage(), response.timestamp());
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<ValidationHandlerResponse> handleConstraintViolationException(
            ConstraintViolationException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        LocalDateTime timestamp = LocalDateTime.now(ZONE_ID);

        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach((error) -> {
            String errorMessage = error.getMessage();
            String fieldName = error.getPropertyPath().toString();
            errors.put(fieldName, errorMessage);
        });
        ValidationHandlerResponse response = new ValidationHandlerResponse(errors, badRequest, timestamp);
        log.info("Constraint Violations: {} at: {}", response.errors(), response.timestamp());
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ValidationHandlerResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        LocalDateTime timestamp = LocalDateTime.now(ZONE_ID);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, errorMessage);
        });
        ValidationHandlerResponse response = new ValidationHandlerResponse(errors, badRequest, timestamp);
        log.info("Method argument not valid exception: {} at: {}", response.errors(), response.timestamp());
        return new ResponseEntity<>(response, badRequest);
    }
}
