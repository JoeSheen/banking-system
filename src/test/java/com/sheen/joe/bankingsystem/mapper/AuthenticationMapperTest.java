package com.sheen.joe.bankingsystem.mapper;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.mapper.impl.AuthenticationMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationMapper authenticationMapper;

    private RegisterRequestDto requestDto;

    private User user;

    @BeforeEach
    void setUp() {
        requestDto = new RegisterRequestDto("Chloe", "Williams",
                LocalDate.of(1986, Month.DECEMBER, 8), "01234567890",
                "chloe.williams@hotmail.com","password");

        user = buildUserForTest();

        authenticationMapper = new AuthenticationMapperImpl(passwordEncoder);
    }

    @Test
    void testToUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User user = authenticationMapper.toUser(requestDto);

        assertNotNull(user);
        assertEquals("Chloe", user.getFirstName());
        assertEquals("Williams", user.getLastName());
        assertEquals(LocalDate.of(1986, Month.DECEMBER, 8), user.getDateOfBirth());
        assertEquals("+44 1234 567890", user.getPhoneNumber());
        assertEquals("chloe.williams@hotmail.com", user.getEmail());
        assertTrue(user.getUsername().contains("ChloeWilliams"));
        assertEquals(19, user.getUsername().length()); // Name length is 13 and random digits 6
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Set.of(UserRole.USER_ROLE), user.getAuthorities());
    }

    @Test
    void testToAuthenticationResponse() {
        AuthenticationResponseDto responseDto =
                authenticationMapper.toAuthenticationResponse(user, "fake-token");

        assertNotNull(responseDto);
        assertEquals(UUID.fromString("1f1deb3c-c3a5-4fac-842c-a729010405c6"), responseDto.id());
        assertEquals("ChloeWilliams0yMtXj", responseDto.username());
        assertEquals("Chloe", responseDto.firstName());
        assertEquals("Williams", responseDto.lastName());
        assertEquals("fake-token", responseDto.token());
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("1f1deb3c-c3a5-4fac-842c-a729010405c6");
        return User.builder().id(id).firstName("Chloe").lastName("Williams")
                .username("ChloeWilliams0yMtXj").build();
    }
}
