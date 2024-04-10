package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.card.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.mapper.AccountCardMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountCardMapperImpl implements AccountCardMapper {

    @Override
    public AccountCardResponseDto toAccountCardResponse(AccountCard card) {
        return new AccountCardResponseDto(card.getId(), card.getCardNumber(),
                card.getCvc(), card.isActive(), card.getDateIssued(),
                card.getExpirationDate(), card.getCardholderName());
    }
}
