package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TransferService {

    TransferResponseDto createTransfer(TransferRequestDto transferRequestDto);

    TransferResponseDto getTransferById(UUID id);

}
