package com.sheen.joe.bankingsystem.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sheen.joe.bankingsystem.controller.AccountController;
import com.sheen.joe.bankingsystem.dto.account.AccountRequestDto;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import com.sheen.joe.bankingsystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.lang.module.ResolutionException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceExceptionHandlerMvcTest {

    private MockMvc mockMvc;

    @Mock
    private AccountController accountController;

    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.fromString("9c42da9f-32ff-422d-8ff4-6fef0cad7c04");

        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new ControllerAdviceExceptionHandler()).build();
    }

    @Test
    void testHandleResourceNotFoundException() throws Exception {
        ResourceNotFoundException exception =
                new ResourceNotFoundException(String.format("Account with ID: %s not found", accountId));
        when(accountController.getById(accountId)).thenThrow(exception);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{accountId}", accountId)
                .accept(MediaType.ALL)).andDo(print()).andExpect(status().isNotFound()).andReturn();

        String expectedErrorMessage =
                "\"errorMessage\":\"Account with ID: 9c42da9f-32ff-422d-8ff4-6fef0cad7c04 not found\"";
        String expectedStatus = "\"status\":\"NOT_FOUND\"";
        assertMvcResult(result, expectedErrorMessage, expectedStatus);
    }

    @Test
    void testHandleInvalidRequestException() throws Exception {
        InvalidRequestException exception = new InvalidRequestException("Invalid account request");
        when(accountController.close(accountId)).thenThrow(exception);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/{accountId}/close", accountId)
                .accept(MediaType.ALL)).andDo(print()).andExpect(status().isBadRequest()).andReturn();

        String expectedErrorMessage ="\"errorMessage\":\"Invalid account request\"";
        String expectedStatus = "\"status\":\"BAD_REQUEST\"";
        assertMvcResult(result, expectedErrorMessage, expectedStatus);
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        AccountRequestDto accountRequestDto = new AccountRequestDto("");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts/{accountId}", accountId)
                .accept(MediaType.ALL).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJsonString(accountRequestDto))).andExpect(status().isBadRequest());
    }

    private void assertMvcResult(MvcResult result, String expectedErrorMessage,
            String expectedStatus) throws UnsupportedEncodingException {
        assertNotNull(result);
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(expectedErrorMessage));
        assertTrue(content.contains(expectedStatus));
    }

    private String toJsonString(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new ResolutionException(e);
        }
    }
}
