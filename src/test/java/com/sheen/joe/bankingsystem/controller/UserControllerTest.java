package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.account.AccountSummaryDto;
import com.sheen.joe.bankingsystem.dto.user.UserResponseDto;
import com.sheen.joe.bankingsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    private final UUID id = UUID.fromString("57f4bce3-d9b4-429b-88df-4b33be40fc6b");

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testUpdateUsername() {
        when(userService.generateNewUsername(any(UUID.class))).thenReturn(buildUserResponseForTest());

        ResponseEntity<UserResponseDto> userResponseEntity = userController.updateUsername(id);
        assertResponseEntity(userResponseEntity);
        assertUserResponseDto(userResponseEntity.getBody());
    }

    @Test
    void testGetAll() {
        when(userService.getAllUsers()).thenReturn(List.of(buildUserResponseForTest()));

        ResponseEntity<List<UserResponseDto>> usersResponseEntity = userController.getAll();
        //asserts on response entity
        assertNotNull(usersResponseEntity);
        assertEquals(usersResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(usersResponseEntity.hasBody());
        // asserts on list
        List<UserResponseDto> users = usersResponseEntity.getBody();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        // asserts on list content
        assertUserResponseDto(users.get(0));
    }

    @Test
    void testGetById() {
        when(userService.getUserById(any(UUID.class))).thenReturn(buildUserResponseForTest());

        ResponseEntity<UserResponseDto> userResponseEntity = userController.getById(id);

        assertResponseEntity(userResponseEntity);
        assertUserResponseDto(userResponseEntity.getBody());
    }

    private void assertResponseEntity(ResponseEntity<UserResponseDto> responseEntity) {
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(responseEntity.hasBody());
    }

    private void assertUserResponseDto(UserResponseDto userResponseDto) {
        assertNotNull(userResponseDto);
        assertEquals(UUID.fromString("57f4bce3-d9b4-429b-88df-4b33be40fc6b"), userResponseDto.id());
        assertEquals("Becky", userResponseDto.firstName());
        assertEquals("Anderson", userResponseDto.lastName());
        assertEquals(LocalDate.of(1977, Month.MARCH, 18), userResponseDto.dateOfBirth());
        assertEquals("+44 1234 567890", userResponseDto.phoneNumber());
        assertEquals("becky.anderson77@gmail.com", userResponseDto.email());
        assertEquals("BeckyAnderson0ITK0q", userResponseDto.username());
        assertEquals(Set.of(buildAccountSummaryDtoForTest()), userResponseDto.accounts());
    }

    private UserResponseDto buildUserResponseForTest() {
        LocalDate dateOfBirth = LocalDate.of(1977, Month.MARCH, 18);
        return new UserResponseDto(id, "Becky", "Anderson", dateOfBirth,
                "+44 1234 567890", "becky.anderson77@gmail.com", "BeckyAnderson0ITK0q",
                Set.of(buildAccountSummaryDtoForTest()));
    }

    private AccountSummaryDto buildAccountSummaryDtoForTest() {
        UUID id = UUID.fromString("d8943509-aeaa-4f9b-b1e2-40cacedb2ed9");
        LocalDateTime updatedAt = LocalDateTime.of(2024, Month.APRIL, 1, 15, 58, 57);
        return new AccountSummaryDto(id, "Test Account",
                new BigDecimal("2500.99"), updatedAt);
    }
}
