package com.sheen.joe.bankingsystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void testIsNullOrEmptyWithNull() {
        assertTrue(StringUtils.isNullOrEmpty(null));
    }

    @Test
    void testIsNullOrEmptyWithEmptyString() {
        assertTrue(StringUtils.isNullOrEmpty(""));
    }

    @Test
    void testIsNullOrEmptyWithValidString() {
        assertFalse(StringUtils.isNullOrEmpty("test string"));
    }

    @Test
    void testCapitaliseFirstLetter() {
        String result = StringUtils.capitaliseFirstLetter("test String");
        assertEquals("Test String", result);
    }
}
