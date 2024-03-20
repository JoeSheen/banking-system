package com.sheen.joe.bankingsystem.service;

import com.sheen.joe.bankingsystem.entity.User;
import com.sheen.joe.bankingsystem.entity.UserRole;
import com.sheen.joe.bankingsystem.repository.UserRepository;
import com.sheen.joe.bankingsystem.security.SecurityUser;
import com.sheen.joe.bankingsystem.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);

        user = buildUserForTest();
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByUsername("JarvisMorrell6AX72P")).thenReturn(Optional.of(user));

        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername("JarvisMorrell6AX72P");

        assertNotNull(securityUser);
        assertEquals(UUID.fromString("511934cb-ad74-4a02-8362-43a8ae49b8d4"), securityUser.getId());
        assertEquals("JarvisMorrell6AX72P", securityUser.getUsername());
        assertEquals("password", securityUser.getPassword());
        assertEquals(Set.of(UserRole.USER_ROLE), securityUser.getAuthorities());
    }

    @Test
    void testLoadUserByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername("JarvisMorrell6AX72P")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("JarvisMorrell6AX72P"));

        String actualMessage = exception.getMessage();
        String expectedMessage = "User with username JarvisMorrell6AX72P not found";

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private User buildUserForTest() {
        UUID id = UUID.fromString("511934cb-ad74-4a02-8362-43a8ae49b8d4");
        return User.builder().id(id).username("JarvisMorrell6AX72P").password("password")
                .authorities(Set.of(UserRole.USER_ROLE)).build();
    }

}
