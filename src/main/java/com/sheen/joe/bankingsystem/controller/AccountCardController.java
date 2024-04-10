package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.card.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.service.AccountCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class AccountCardController {

    private final AccountCardService accountCardService;

    @GetMapping(path = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountCardResponseDto> getById(@PathVariable("cardId")UUID id) {
        AccountCardResponseDto accountCardResponseDto = accountCardService.getAccountCardById(id);
        log.info("Account card with ID: {} found", accountCardResponseDto.id());
        return new ResponseEntity<>(accountCardResponseDto, HttpStatus.OK);
    }

    @PutMapping(path = "/{cardId}/deactivate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountCardResponseDto> deactivate(@PathVariable("cardId") UUID id) {
        AccountCardResponseDto accountCardResponseDto = accountCardService.deactivateAccountCard(id);
        log.info("Account card with ID: {} deactivated", accountCardResponseDto.id());
        return new ResponseEntity<>(accountCardResponseDto, HttpStatus.OK);
    }
}
