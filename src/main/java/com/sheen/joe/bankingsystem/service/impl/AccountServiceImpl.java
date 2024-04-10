package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AccountMapper;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.security.SecurityUtils;
import com.sheen.joe.bankingsystem.service.AccountService;
import com.sheen.joe.bankingsystem.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AccountMapper accountMapper;

    private static final String ACCOUNT_EXCEPTION_MSG = "Invalid account request";

    @Override
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with ID: %s not found", userId)));
        Account account = buildAccountForUser(accountRequestDto, user);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponseDto updateAccount(UUID id, AccountRequestDto accountRequestDto) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account with ID: %s not found", id)));
        if (isNotUserAccount(account.getUser().getId())) {
            throw new InvalidRequestException(ACCOUNT_EXCEPTION_MSG);
        }
        account.setAccountName(accountRequestDto.accountName());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public Page<AccountResponseDto> getAllUserAccounts(int pageNumber, int pageSize, boolean closed, String sortProperty) {
        Sort sort = Sort.by(new Order(Sort.Direction.DESC, sortProperty));
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        return accountRepository.findAllUserAccounts(closed, userId, paging).map(accountMapper::toAccountResponse);
    }

    @Override
    public AccountResponseDto getAccountById(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account with ID: %s not found", id)));
        if (isNotUserAccount(account.getUser().getId())) {
            throw new InvalidRequestException(ACCOUNT_EXCEPTION_MSG);
        }
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Pair<Boolean, String> closeAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account with ID: %s not found", id)));
        if (isNotUserAccount(account.getUser().getId())) {
            throw new InvalidRequestException(ACCOUNT_EXCEPTION_MSG);
        }
        if (!account.getBalance().equals(BigDecimal.ZERO)) {
            return Pair.of(false, "Account balance must be 0.00");
        }
        account.setClosed(true);
        accountRepository.save(account);
        String message = String.format("Account with ID: %s successfully closed", id);
        return Pair.of(true, message);
    }

    @Override
    public AccountResponseDto requestNewCardForAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Account with ID: %s not found", id)));
        if (isNotUserAccount(account.getUser().getId())) {
            throw new InvalidRequestException(ACCOUNT_EXCEPTION_MSG);
        }
        User user = account.getUser();
        AccountCard newCard = buildAccountCard(account, user.getFirstName(), user.getLastName());
        account.setAccountCard(newCard);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    private boolean isNotUserAccount(UUID accountUserId) {
        return !accountUserId.equals(SecurityUtils.getUserIdFromSecurityContext());
    }

    private Account buildAccountForUser(AccountRequestDto accountRequestDto, User user) {
        Account account = accountMapper.toAccount(accountRequestDto);
        account.setAccountNumber(StringUtils.generateRandomNumeric(8));
        AccountCard card = buildAccountCard(account, user.getFirstName(), user.getLastName());
        account.setAccountCard(card);
        account.setUser(user);
        return account;
    }

    private AccountCard buildAccountCard(Account account, String firstName, String lastName) {
        String cardNumber = StringUtils.generateAndFormatCardNumber();
        String cvc = StringUtils.generateRandomNumeric(3);
        LocalDate dateIssued = LocalDate.now(ZoneId.of("UTC"));
        LocalDate expirationDate = dateIssued.plusYears(4);
        String cardholder = firstName + " " + lastName;

        return AccountCard.builder().cardNumber(cardNumber)
                .cvc(cvc).isActive(true).dateIssued(dateIssued)
                .expirationDate(expirationDate).account(account)
                .cardholderName(cardholder.toUpperCase()).build();
    }

}
