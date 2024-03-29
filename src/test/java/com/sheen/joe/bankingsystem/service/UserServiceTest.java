package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
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
    void testGetUserById() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(buildUserForTest()));

        UserResponseDto responseDto = userService.getUserById(id);
        assertNotNull(responseDto);
        assertEquals(UUID.fromString("056b50b2-656a-4adc-bcf5-0903163475a9"), responseDto.id());
        assertEquals("Laura", responseDto.firstName());
        assertEquals("Sparrow", responseDto.lastName());
        assertEquals(LocalDate.of(2000, Month.MARCH, 29), responseDto.dateOfBirth());
        assertEquals("+44 1234 567890", responseDto.phoneNumber());
        assertEquals("sparrow.laura@gmail.com", responseDto.email());
        assertEquals("LauraSparrow1wjAtk", responseDto.username());
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

    private User buildUserForTest() {
        LocalDate dateOfBirth = LocalDate.of(2000, Month.MARCH, 29);
        return User.builder().id(id).firstName("Laura").lastName("Sparrow")
                .dateOfBirth(dateOfBirth).phoneNumber("+44 1234 567890")
                .email("sparrow.laura@gmail.com").username("LauraSparrow1wjAtk")
                .build();
    }
}