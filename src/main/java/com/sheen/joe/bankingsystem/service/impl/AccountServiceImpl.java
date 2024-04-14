package com.sheen.joe.bankingsystem.service.impl;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.dto.account.AccountResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.AccountCard;
import com.sheen.joe.bankingsystem.entity.User;
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

    private static final String ACCOUNT_EXCEPTION_MSG = "Account with ID: %s not found";

    @Override
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ACCOUNT_EXCEPTION_MSG, userId)));
        Account account = buildAccountForUser(accountRequestDto, user);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponseDto updateAccount(UUID id, AccountRequestDto accountRequestDto) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        Account account = accountRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ACCOUNT_EXCEPTION_MSG, id)));
        account.setAccountName(accountRequestDto.accountName());
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public CollectionResponseDto<AccountResponseDto> getAllUserAccounts(int pageNumber, int pageSize,
            boolean closed, String sortProperty) {
        Sort sort = Sort.by(new Order(Sort.Direction.DESC, sortProperty));
        Pageable paging = PageRequest.of(pageNumber, pageSize, sort);
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();

        Page<AccountResponseDto> page = accountRepository.findAllUserAccounts(closed, userId, paging)
                .map(accountMapper::toAccountResponse);

        return new CollectionResponseDto<>(page.getContent(), page.getNumber(), page.getTotalPages(),
                page.getTotalElements(), page.getSort().isSorted());
    }

    @Override
    public AccountResponseDto getAccountById(UUID id) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        Account account = accountRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ACCOUNT_EXCEPTION_MSG, id)));
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public Pair<Boolean, String> closeAccount(UUID id) {
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        Account account = accountRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ACCOUNT_EXCEPTION_MSG, id)));
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
        UUID userId = SecurityUtils.getUserIdFromSecurityContext();
        Account account = accountRepository.findByIdAndUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ACCOUNT_EXCEPTION_MSG, id)));
        User user = account.getUser();
        AccountCard newCard = buildAccountCard(account, user.getFirstName(), user.getLastName());
        account.setAccountCard(newCard);
        return accountMapper.toAccountResponse(accountRepository.save(account));
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
