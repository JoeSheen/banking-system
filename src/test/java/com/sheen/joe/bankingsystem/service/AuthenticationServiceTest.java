package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import com.sheen.joe.bankingsystem.mapper.AuthenticationMapper;
import com.sheen.joe.bankingsystem.mapper.impl.AuthenticationMapperImpl;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private AuthenticationService authenticationService;

    private final UUID id = UUID.fromString("3278535e-9757-4ec9-bd72-2fc3f92b8d3b");

    private RegisterRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new RegisterRequestDto("Elizabeth", "Fleming",
                LocalDate.of(1984, Month.JANUARY, 25), "01234567890",
                "elizabeth.fleming@hotmail.com", "password");

        AuthenticationMapper mapper = new AuthenticationMapperImpl(passwordEncoder);
        authenticationService = new AuthenticationServiceImpl(userRepository, jwtUtils, mapper, authenticationManager);
    }

    @Test
    void testRegisterUser() {
        when(userRepository.existsByPhoneNumber("01234567890")).thenReturn(false);
        when(userRepository.existsByEmail("elizabeth.fleming@hotmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(buildUserForTest());
        when(jwtUtils.generateToken("ElizabethFleming6AUTu3")).thenReturn("fake-token");

        AuthenticationResponseDto responseDto = authenticationService.registerUser(requestDto);
        assertAuthenticationResponseDto(responseDto);
    }

    @Test
    void testRegisterUserThrowsInvalidRequestException() {
        when(userRepository.existsByPhoneNumber("01234567890")).thenReturn(true);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                authenticationService.registerUser(requestDto));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Phone number or email already registered";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testLoginUser() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(buildUserForTest()));
        when(jwtUtils.generateToken("ElizabethFleming6AUTu3")).thenReturn("fake-token");

        AuthenticationResponseDto responseDto = authenticationService.loginUser(
                new LoginRequestDto("ElizabethFleming6AUTu3", "password"));
        assertAuthenticationResponseDto(responseDto);
    }

    @Test
    void testLoginUserThrowsResourceNotFoundException() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(buildSecurityUserForTest());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        LoginRequestDto loginRequestDto = new LoginRequestDto("ElizabethFleming6AUTu3", "password");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                authenticationService.loginUser(loginRequestDto));

        String actualMessage = exception.getMessage();
        String expectedMessage = "User with ID: "+ id + " not found";
        assertTrue(actualMessage.contains(expectedMessage));

    }

    private void assertAuthenticationResponseDto(AuthenticationResponseDto authenticationResponseDto) {
        assertNotNull(authenticationResponseDto);
        assertEquals(UUID.fromString("3278535e-9757-4ec9-bd72-2fc3f92b8d3b"), authenticationResponseDto.id());
        assertEquals("ElizabethFleming6AUTu3", authenticationResponseDto.username());
        assertEquals("Elizabeth", authenticationResponseDto.firstName());
        assertEquals("Fleming", authenticationResponseDto.lastName());
        assertEquals("fake-token", authenticationResponseDto.token());
    }

    private SecurityUser buildSecurityUserForTest() {
        return new SecurityUser(id, Set.of(UserRole.USER_ROLE),
                "password", "ElizabethFleming6AUTu3");
    }

    private User buildUserForTest() {
        return User.builder().id(id).firstName("Elizabeth").lastName("Fleming")
                .username("ElizabethFleming6AUTu3").build();
    }

}
