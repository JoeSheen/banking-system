package com.sheen.joe.bankingsystem.controller;

import com.sheen.joe.bankingsystem.dto.authentication.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.authentication.LoginRequestDto;
import com.sheen.joe.bankingsystem.dto.authentication.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.Country;
import com.sheen.joe.bankingsystem.security.JwtUtils;
import com.sheen.joe.bankingsystem.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtUtils jwtUtils;

    private AuthenticationController authenticationController;

    private AuthenticationResponseDto authenticationResponseDto;

    @BeforeEach
    void setUp() {
        authenticationResponseDto = buildAuthenticationResponse();
        authenticationController = new AuthenticationController(authenticationService, jwtUtils);
    }

    @Test
    void testRegister() {
        when(authenticationService.registerUser(any(RegisterRequestDto.class)))
                .thenReturn(authenticationResponseDto);
        when(jwtUtils.generateTokenCookie("MichaelWood1A7yCh")).thenReturn("fake-token-cookie");

        RegisterRequestDto requestDto = new RegisterRequestDto("Michael", "Wood",
                LocalDate.of(2003, Month.FEBRUARY, 5), Country.UK, "01234567890",
                "wood.michael@outlook.com", "password");

        ResponseEntity<AuthenticationResponseDto> authenticationResponseEntity =
                authenticationController.register(requestDto);

        assertResponseEntity(authenticationResponseEntity, HttpStatus.CREATED);
        assertEquals(buildExpectedHeadersForTest(), authenticationResponseEntity.getHeaders());
        assertAuthenticationResponse(authenticationResponseEntity.getBody());
    }

    @Test
    void testLogin() {
        when(authenticationService.loginUser(any(LoginRequestDto.class)))
                .thenReturn(authenticationResponseDto);
        when(jwtUtils.generateTokenCookie("MichaelWood1A7yCh")).thenReturn("fake-token-cookie");

        LoginRequestDto requestDto = new LoginRequestDto("MichaelWood1A7yCh", "password");

        ResponseEntity<AuthenticationResponseDto> authenticationResponseEntity =
                authenticationController.login(requestDto);

        assertResponseEntity(authenticationResponseEntity, HttpStatus.OK);
        assertEquals(buildExpectedHeadersForTest(), authenticationResponseEntity.getHeaders());
        assertAuthenticationResponse(authenticationResponseEntity.getBody());
    }

    private void assertResponseEntity(ResponseEntity<AuthenticationResponseDto> responseEntity, HttpStatus status) {
        assertNotNull(responseEntity);
        assertEquals(status, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    private void assertAuthenticationResponse(AuthenticationResponseDto authenticationResponseDto) {
        assertNotNull(authenticationResponseDto);
        assertEquals(UUID.fromString("d916bbf0-7147-45fd-842f-8d92239dd00e"), authenticationResponseDto.id());
        assertEquals("MichaelWood1A7yCh", authenticationResponseDto.username());
        assertEquals("Michael", authenticationResponseDto.firstName());
        assertEquals("Wood", authenticationResponseDto.lastName());
    }

    private AuthenticationResponseDto buildAuthenticationResponse() {
        UUID id = UUID.fromString("d916bbf0-7147-45fd-842f-8d92239dd00e");
        return new AuthenticationResponseDto(id, "MichaelWood1A7yCh", "Michael", "Wood");
    }

    private HttpHeaders buildExpectedHeadersForTest() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, "fake-token-cookie");
        return new HttpHeaders(headers);
    }
}
