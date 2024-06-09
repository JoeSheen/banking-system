package com.sheen.joe.bankingsystem;

import com.sheen.joe.bankingsystem.entity.*;
import com.sheen.joe.bankingsystem.repository.AccountRepository;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("--- DataLoader Started ---");
        addUsers();
        addAccounts();
        userRepository.findAll().forEach(user -> log.info("ID: {} username: {}", user.getId(), user.getUsername()));
        log.info("--- DataLoader Finished ---");
    }

    private void addUsers() {
        log.info("Adding users...");
        userRepository.save(new User(UUID.randomUUID(), "Super", "User", LocalDate.of(1986, Month.JUNE, 9),
                Country.UK, formatPhoneNumber("123456789", "GB"), "admin@outlook.co.uk",
                "adminABC123", passwordEncoder.encode("Password123!"),
                Set.of(UserRole.USER_ROLE, UserRole.ADMIN_ROLE), Set.of()));

        Stream.of("Adam Amos", "Kim Williams", "Rosie Henderson", "Bryce Moring").forEach(names -> {
            String[] splitNames = names.split(" ");
            userRepository.save(buildUser(splitNames));
        });
        log.info("{} users added", userRepository.findAll().size());
    }

    private void addAccounts() {
        log.info("Creating accounts...");
        userRepository.findAll().forEach(user -> {
            if (!user.getUsername().contains("admin")) {
                accountRepository.save(buildAccount(user));
            }
        });
        log.info("{} accounts created", accountRepository.findAll().size());
    }

    private User buildUser(String[] splitNames) {
        String email = splitNames[0] + "." + splitNames[1] + "@gmail.com";
        String password = passwordEncoder.encode("Password123!");
        String username = splitNames[0] + splitNames[1] + StringUtils.generateRandomAlphanumeric(6, true);
        Country country = Country.UK;
        String phoneNumber = formatPhoneNumber(StringUtils.generateRandomNumeric(9), country.getCountryCode());
        LocalDate dateOfBirth = LocalDate.of(1992, Month.JUNE, 20);
        return new User(UUID.randomUUID(), splitNames[0], splitNames[1], dateOfBirth, country, phoneNumber, email,
                username, password, Set.of(UserRole.USER_ROLE), Set.of());
    }

    private String formatPhoneNumber(String phoneNumberStr, String countryCode) {
        return StringUtils.formatPhoneNumberString("07" + phoneNumberStr, countryCode);
    }

    private Account buildAccount(User user) {
        AccountCard card = new AccountCard();
        String accountNumber = StringUtils.generateRandomNumeric(8);
        String sortCode = StringUtils.generateSortCode();
        LocalDateTime now = LocalDateTime.now();
        return new Account(UUID.randomUUID(), "Current Account", accountNumber, card, new BigDecimal("150000.00"),
                sortCode, false, user, List.of(), List.of(), now, now);
    }
}
