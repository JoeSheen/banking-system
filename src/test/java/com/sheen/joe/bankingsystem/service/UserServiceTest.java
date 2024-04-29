package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.CollectionResponseDto;
import com.sheen.joe.bankingsystem.dto.account.AccountSummaryDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.Account;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.UserMapper;
import com.sheen.joe.bankingsystem.mapper.impl.UserMapperImpl;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private final UUID id = UUID.fromString("056b50b2-656a-4adc-bcf5-0903163475a9");

    @BeforeEach
    void setUp() {
        UserMapper userMapper = new UserMapperImpl();
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void testGenerateNewUsername() {
        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(buildUserForTest("LauraSparrow1wjAtk")));
        when(userRepository.save(any(User.class))).thenReturn(buildUserForTest("LauraSparrow78hJGd"));

        UserResponseDto responseDto = userService.generateNewUsername(id);
        assertUserResponseDto(responseDto, "LauraSparrow78hJGd");
    }

    @Test
    void testGenerateNewUsernameThrowsResourceNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.generateNewUsername(id));

        String actualMessage = exception.getMessage();
        String expectedMessage = "User with ID: " + id + " not found";

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(buildUserForTest("LauraSparrow1wjAtk")));

        UserResponseDto responseDto = userService.getUserById(id);
        assertUserResponseDto(responseDto, "LauraSparrow1wjAtk");
    }

    @Test
    void testGetUserByIdThrowsResourceNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById(id));

        String actualMessage = exception.getMessage();
        String expectedMessage = "User with ID: " + id + " not found";

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAllUsers() {
        Page<User> userPage = new PageImpl<>(List.of(buildUserForTest("LauraSparrow1wjAtk")));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        CollectionResponseDto<UserResponseDto> collectionResponseDto = userService.getAllUsers(0, 25);

        // asserts collection response dto
        assertNotNull(collectionResponseDto);
        assertEquals(1, collectionResponseDto.content().size());
        assertEquals(0, collectionResponseDto.currentPage());
        assertEquals(1, collectionResponseDto.totalPages());
        assertEquals(1, collectionResponseDto.totalElements());
        // asserts collection content (UserResponseDto)
        assertUserResponseDto(collectionResponseDto.content().get(0), "LauraSparrow1wjAtk");
    }

    private User buildUserForTest(String username) {
        LocalDate dateOfBirth = LocalDate.of(2000, Month.MARCH, 29);
        return User.builder().id(id).firstName("Laura").lastName("Sparrow")
                .dateOfBirth(dateOfBirth).phoneNumber("+44 1234 567890")
                .email("sparrow.laura@gmail.com").username(username)
                .accounts(Set.of(buildAccountForTest())).build();
    }

    private void assertUserResponseDto(UserResponseDto userResponseDto, String expectedUsername) {
        assertNotNull(userResponseDto);
        assertEquals(UUID.fromString("056b50b2-656a-4adc-bcf5-0903163475a9"), userResponseDto.id());
        assertEquals("Laura", userResponseDto.firstName());
        assertEquals("Sparrow", userResponseDto.lastName());
        assertEquals(LocalDate.of(2000, Month.MARCH, 29), userResponseDto.dateOfBirth());
        assertEquals("+44 1234 567890", userResponseDto.phoneNumber());
        assertEquals("sparrow.laura@gmail.com", userResponseDto.email());
        assertEquals(expectedUsername, userResponseDto.username());
        assertEquals(Set.of(expectedAccountSummaryDto()), userResponseDto.accounts());
    }

    private Account buildAccountForTest() {
        UUID id = UUID.fromString("5d6658ce-7a97-48a5-bb18-0cde8cdfe32c");
        LocalDateTime updatedAt = LocalDateTime.of(2024, Month.APRIL, 1, 16, 6, 36);
        return Account.builder().id(id).accountName("Test Account")
                .balance(BigDecimal.ONE).updatedAt(updatedAt).build();
    }

    private AccountSummaryDto expectedAccountSummaryDto() {
        UUID id = UUID.fromString("5d6658ce-7a97-48a5-bb18-0cde8cdfe32c");
        LocalDateTime updatedAt = LocalDateTime.of(2024, Month.APRIL, 1, 16, 6, 36);
        return new AccountSummaryDto(id, "Test Account", BigDecimal.ONE, updatedAt);
    }
}
