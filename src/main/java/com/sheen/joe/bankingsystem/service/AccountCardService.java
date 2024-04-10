package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.card.AccountCardResponseDto;

import java.util.UUID;

public interface AccountCardService {

    AccountCardResponseDto getAccountCardById(UUID id);

    AccountCardResponseDto deactivateAccountCard(UUID id);
}
