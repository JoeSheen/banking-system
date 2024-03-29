package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Transfer;

public interface TransferMapper {

    Transfer toTransfer(TransferRequestDto transferRequestDto);

    TransferResponseDto toTransferResponse(Transfer transfer);
}
