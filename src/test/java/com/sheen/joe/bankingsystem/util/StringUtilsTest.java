package com.sheen.joe.bankingsystem.util;

import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
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

    @Test
    void testGenerateRandomAlphanumericStaringWithNumber() {
        String result = StringUtils.generateRandomAlphanumeric(10, true);

        assertEquals(10, result.length());
        assertTrue(Character.isDigit(result.charAt(0)));
    }

    @Test
    void testGenerateRandomAlphanumeric() {
        String result = StringUtils.generateRandomAlphanumeric(10, false);

        assertEquals(10, result.length());
        assertTrue(Character.isLetterOrDigit(result.charAt(0)));
    }

    @Test
    void testFormatPhoneNumberString() {
        String result = StringUtils.formatPhoneNumberString("01234567890");
        assertEquals("+44 1234 567890", result);
    }

    @Test
    void testFormatPhoneNumberStringThrowsException() {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                StringUtils.formatPhoneNumberString(null));

        String actualMessage = exception.getMessage();
        String expectedMessage = "Phone number contains an invalid character(s)";

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
