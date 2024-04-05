package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.AccountCardResponseDto;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AccountCardMapper;
import com.sheen.joe.bankingsystem.repository.AccountCardRepository;
import com.sheen.joe.bankingsystem.security.SecurityUtils;
import com.sheen.joe.bankingsystem.service.AccountCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountCardServiceImpl implements AccountCardService {

    private final AccountCardRepository accountCardRepository;

    private final AccountCardMapper accountCardMapper;

    @Override
    public AccountCardResponseDto getAccountCardById(UUID id) {
        AccountCard card = accountCardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account card with ID: %s not found", id)));
        if (!card.getAccount().getUser().getId().equals(SecurityUtils.getUserIdFromSecurityContext())) {
            throw new InvalidRequestException("Invalid Request");
        }
        return accountCardMapper.toAccountCardResponse(card);
    }

    @Override
    public AccountCardResponseDto deactivateAccountCard(UUID id) {
        AccountCard card = accountCardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account card with ID: %s not found", id)));
        if (!card.getAccount().getUser().getId().equals(SecurityUtils.getUserIdFromSecurityContext())) {
            throw new InvalidRequestException("Invalid Request");
        }
        card.setActive(false);
        return accountCardMapper.toAccountCardResponse(accountCardRepository.save(card));
    }
}
