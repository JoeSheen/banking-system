package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Transfer;

public interface TransferMapper {

    Transfer toTransfer(TransferRequestDto transferRequestDto);

    TransferResponseDto toTransferResponse(Transfer transfer);
}
