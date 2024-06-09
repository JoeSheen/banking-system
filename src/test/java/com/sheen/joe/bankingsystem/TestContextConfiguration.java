package com.sheen.joe.bankingsystem;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestContextConfiguration {

    @MockBean
    private DataLoader dataLoader;
}
