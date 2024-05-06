package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.service.TransferService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping(path = {"/deposit", "/withdraw"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDto> performDepositOrWithdraw(@Valid @RequestBody DepositWithdrawTransferRequestDto transferRequestDto) {
        TransferResponseDto transferResponseDto = transferService.createTransfer(transferRequestDto);
        log.info("{} with ID: {} created", transferResponseDto.reference(), transferResponseDto.id());
        return new ResponseEntity<>(transferResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDto> performTransfer(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        TransferResponseDto transferResponseDto = transferService.createTransfer(transferRequestDto);
        log.info("Account to Account transfer with ID: {} created", transferResponseDto.id());
        return new ResponseEntity<>(transferResponseDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{transferId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDto> getById(@PathVariable("transferId") UUID id) {
        TransferResponseDto transferResponseDto = transferService.getTransferById(id);
        log.info("Transfer with ID: {} found", transferResponseDto.id());
        return new ResponseEntity<>(transferResponseDto, HttpStatus.OK);
    }
}
