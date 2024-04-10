package com.sheen.joe.bankingsystem.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.sheen.joe.bankingsystem.exception.InvalidRequestException;
import org.apache.commons.lang3.RandomStringUtils;

public final class StringUtils {

    private StringUtils() {}

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String capitaliseFirstLetter(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String generateRandomAlphanumeric(int length, boolean startWithNumber) {
        if (startWithNumber) {
            return RandomStringUtils.randomNumeric(1) + RandomStringUtils.randomAlphanumeric(length - 1);
        }
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String generateRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static String generateAndFormatCardNumber() {
        String cardDigits = generateRandomNumeric(16);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardDigits.length(); i++) {
            if (i != 0 && i % 4 == 0) {
                sb.append(" ");
            }
            sb.append(cardDigits.charAt(i));
        }
        return sb.toString();
    }

    public static String formatPhoneNumberString(String phoneNumberStr, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumber = phoneNumberUtil.parse(phoneNumberStr, countryCode);
            return phoneNumberUtil.format(phoneNumber, PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

}
