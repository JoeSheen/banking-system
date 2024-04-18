package com.sheen.joe.bankingsystem.util;

import com.sheen.joe.bankingsystem.entity.Country;
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
    void testGenerateRandomNumeric() {
        String result = StringUtils.generateRandomNumeric(16);
        assertEquals(16, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c));
        }
    }

    @Test
    void testGenerateAndFormatCardNumber() {
        String result = StringUtils.generateAndFormatCardNumber();
        assertEquals(19, result.length()); // 16 digits + 3 spaces
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c) || Character.isWhitespace(c));
        }
    }

    @Test
    void testGenerateSortCode() {
        String result = StringUtils.generateSortCode();

        assertEquals(8, result.length());
        for (char c : result.toCharArray()) {
            assertTrue(Character.isDigit(c) || c == '-');
        }
    }

    @Test
    void testFormatPhoneNumberString() {
        String result = StringUtils.formatPhoneNumberString("01234567890", Country.UK.getCountryCode());
        assertEquals("+44 1234 567890", result);
    }

    @Test
    void testFormatPhoneNumberStringThrowsException() {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                StringUtils.formatPhoneNumberString(null, Country.UK.getCountryCode()));

        String actualMessage = exception.getMessage();
        String expectedMessage = "The phone number supplied was null";

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
