package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.card.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.entity.AccountCard;

public interface AccountCardMapper {

    AccountCardResponseDto toAccountCardResponse(AccountCard card);

}
