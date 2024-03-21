package com.sheen.joe.bankingsystem.mapper.impl;

import com.sheen.joe.bankingsystem.dto.AuthenticationResponseDto;
import com.sheen.joe.bankingsystem.dto.RegisterRequestDto;
import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.mapper.AuthenticationMapper;
import com.sheen.joe.bankingsystem.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationMapperImpl implements AuthenticationMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User toUser(RegisterRequestDto registerRequestDto) {
        String firstName = registerRequestDto.firstName();
        String lastName = registerRequestDto.lastName();
        String username = firstName + lastName + StringUtils.generateRandomAlphanumeric(6, true);
        String phoneNumber = StringUtils.formatPhoneNumberString(registerRequestDto.phoneNumber());

        return User.builder().firstName(firstName).lastName(lastName)
                .dateOfBirth(registerRequestDto.dateOfBirth()).phoneNumber(phoneNumber)
                .email(registerRequestDto.email()).username(username)
                .password(passwordEncoder.encode(registerRequestDto.password()))
                .authorities(Set.of(UserRole.USER_ROLE)).build();
    }

    @Override
    public AuthenticationResponseDto toAuthenticationResponse(User user, String token) {
        return new AuthenticationResponseDto(user.getId(), user.getFirstName(), user.getLastName(), token);
    }
}
