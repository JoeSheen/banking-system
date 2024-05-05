package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;
import com.sheen.joe.bankingsystem.entity.TransferCategory;
import com.sheen.joe.bankingsystem.mapper.TransferMapper;
import com.sheen.joe.bankingsystem.util.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TransferMapperImpl implements TransferMapper {

    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    @Override
    public Transfer toTransfer(DepositWithdrawTransferRequestDto depositWithdrawTransferRequestDto, Account account) {
        String reference = depositWithdrawTransferRequestDto.transferType().toString().toLowerCase();
        TransferCategory category = TransferCategory.GENERAL;

        return Transfer.builder().transferType(depositWithdrawTransferRequestDto.transferType())
                .amount(depositWithdrawTransferRequestDto.amount()).reference(StringUtils.capitaliseFirstLetter(reference))
                .category(category).senderAccount(account).receiverAccount(account).timestamp(LocalDateTime.now(ZONE_ID))
                .build();
    }

    @Override
    public Transfer toTransfer(TransferRequestDto transferRequestDto, Account senderAccount, Account receiverAccount) {
        TransferCategory category = transferRequestDto.category() != null ?
                transferRequestDto.category() : TransferCategory.GENERAL;

        return Transfer.builder().transferType(transferRequestDto.transferType())
                .amount(transferRequestDto.amount()).reference(transferRequestDto.reference())
                .category(category).senderAccount(senderAccount).receiverAccount(receiverAccount)
                .timestamp(LocalDateTime.now(ZONE_ID)).build();
    }

    @Override
    public TransferResponseDto toTransferResponse(Transfer transfer, char symbol) {
        return new TransferResponseDto(transfer.getId(), transfer.getTransferType(),
                transfer.getAmount(), transfer.getReference(), transfer.getCategory(),
                transfer.getTimestamp(), symbol);
    }
}
