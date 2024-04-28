package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TransferService {

    TransferResponseDto createTransfer(DepositWithdrawTransferRequestDto transferRequestDto);

    TransferResponseDto createTransfer(TransferRequestDto transferRequestDto);

    TransferResponseDto getTransferById(UUID id);

}
