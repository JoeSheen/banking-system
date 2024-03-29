package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.mapper.TransferMapper;
import org.springframework.stereotype.Component;

@Component
public class TransferMapperImpl implements TransferMapper {

    @Override
    public Transfer toTransfer(TransferRequestDto transferRequestDto) {
        return Transfer.builder().transferType(transferRequestDto.transferType())
                .amount(transferRequestDto.amount()).reference(transferRequestDto.reference()).build();
    }

    @Override
    public TransferResponseDto toTransferResponse(Transfer transfer) {
        return new TransferResponseDto(transfer.getId(), transfer.getAmount(), transfer.getTimestamp());
    }
}
