package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.UserResponseDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.mapper.impl.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private User user;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        user = buildUserForTest();
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUserResponse() {
        UserResponseDto responseDto = userMapper.toUserResponse(user);
        assertNotNull(responseDto);
        assertEquals(UUID.fromString("5d0d6908-40f6-4c3a-9390-2ddf40ae6437"), responseDto.id());
        assertEquals("James", responseDto.firstName());
        assertEquals("Tanner", responseDto.lastName());
        assertEquals(LocalDate.of(1974, Month.JULY, 25), responseDto.dateOfBirth());
        assertEquals("james.tanner@protonmail.com", responseDto.email());
        assertEquals("+44 1234 567890", responseDto.phoneNumber());
        assertEquals("JamesTanner5pMMcU", responseDto.username());
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("5d0d6908-40f6-4c3a-9390-2ddf40ae6437");
        LocalDate dateOfBirth = LocalDate.of(1974, Month.JULY, 25);
        return User.builder().id(id).firstName("James").lastName("Tanner").dateOfBirth(dateOfBirth)
                .email("james.tanner@protonmail.com").phoneNumber("+44 1234 567890")
                .username("JamesTanner5pMMcU").build();
    }
}
