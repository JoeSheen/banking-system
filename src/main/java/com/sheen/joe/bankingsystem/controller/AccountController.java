package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDto> create(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        AccountResponseDto accountResponseDto = accountService.createAccount(accountRequestDto);
        log.info("Account with ID: {} created", accountResponseDto.id());
        return new ResponseEntity<>(accountResponseDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDto> update(@PathVariable("accountId") UUID id,
            @Valid @RequestBody AccountRequestDto accountRequestDto) {
        AccountResponseDto accountResponseDto = accountService.updateAccount(id, accountRequestDto);
        log.info("Account with ID: {} updated", accountResponseDto.id());
        return new ResponseEntity<>(accountResponseDto, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponseDto<AccountResponseDto>> getAll(@RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "false") Boolean closed,
            @RequestParam(defaultValue = "updatedAt") String sortProperty) {
        CollectionResponseDto<AccountResponseDto> responseDto = accountService
                .getAllUserAccounts(pageNumber, pageSize, closed, sortProperty);
        log.info("Page {} contains {} account(s)", responseDto.currentPage(), responseDto.content().size());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDto> getById(@PathVariable("accountId") UUID id) {
        AccountResponseDto accountResponseDto = accountService.getAccountById(id);
        log.info("Account with ID: {} found", accountResponseDto.id());
        return new ResponseEntity<>(accountResponseDto, HttpStatus.OK);
    }

    @PutMapping(path = "/{accountId}/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> close(@PathVariable("accountId") UUID id) {
        Pair<Boolean, String> pair = accountService.closeAccount(id);
        HttpStatus status;
        if (!pair.getLeft()) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.OK;
        }
        String message = pair.getRight();
        log.info(message);
        return new ResponseEntity<>(message, status);
    }

    @PutMapping(path = "/{accountId}/update-card", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponseDto> newCardForAccount(@PathVariable("accountId") UUID id) {
        AccountResponseDto accountResponseDto = accountService.requestNewCardForAccount(id);
        log.info("New card added to account: {}", accountResponseDto.id());
        return new ResponseEntity<>(accountResponseDto, HttpStatus.OK);
    }
}
