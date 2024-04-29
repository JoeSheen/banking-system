package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.transfer.DepositWithdrawTransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferRequestDto;
import com.sheen.joe.bankingsystem.dto.transfer.TransferResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.Transfer;

public interface TransferMapper {

    Transfer toTransfer(DepositWithdrawTransferRequestDto depositWithdrawTransferRequestDto, Account account);

    Transfer toTransfer(TransferRequestDto transferRequestDto, Account senderAccount, Account receiverAccount);

    TransferResponseDto toTransferResponse(Transfer transfer, char symbol);
}
