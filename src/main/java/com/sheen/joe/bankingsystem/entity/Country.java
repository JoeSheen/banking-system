package com.sheen.joe.bankingsystem.entity;

public enum Country {
    BELGIUM("BE"),
    CANADA("CA"),
    FRANCE("FR"),
    GERMANY("DE"),
    ITALY("IT"),
    UK("GB"),
    USA("US");

    private final String countryCode;

    Country(String code) {
        this.countryCode = code;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
