package com.example.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringFormatter {

    private static final Set<String> NAME_PREFIXES = new HashSet<>(Arrays.asList(
            "Mc", "Mac", "O'", "D'", "De", "Van", "Von", "Le", "La", "Da", "Di", "Du", "Del",
            "De La", "Van Der", "De Los", "St.", "San", "Dos"
    ));

    private static final Set<String> PROVINCE_STATE_COUNTRY_CODES = Set.of(
            // Canadian Provinces
            "AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT",
            // U.S. States and Territories
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL",
            "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
            "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
            "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", "DC",
            // Countries
            "US", "CA", "GB", "FR", "DE", "IT", "ES", "AU", "JP", "CN", "IN", "RU", "ZA",
            "USA", "CAN", "GBR", "FRA", "DEU", "ITA", "ESP", "AUS", "JPN", "CHN", "IND",
            "RUS", "ZAF"
    );

    private static final Set<String> COUNTRY_CODES_AND_NAMES = Set.of(
            // Full Country Names
            "United States", "United States of America", "Canada", "United Kingdom",
            "France", "Germany", "Italy", "Spain", "Australia", "Japan", "China",
            "India", "Russia", "South Africa"
    );

    // Format Names (First Name, Last Name, Full Name)
    public static String formatName(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        String[] parts = input.split("\\s+");
        return Arrays.stream(parts)
                .map(StringFormatter::capitalizeNameWord)
                .reduce("", (a, b) -> a + " " + b).trim();
    }

    private static String capitalizeNameWord(String word) {
        for (String prefix : NAME_PREFIXES) {
            if (word.toUpperCase().startsWith(prefix.toUpperCase()) && word.length() > prefix.length()) {
                return prefix + capitalizeWord(word.substring(prefix.length()));
            }
        }
        return capitalizeWord(word);
    }

    // Format Addresses
    public static String formatAddress(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        String[] parts = input.split("(?=[,])|(?<=[,])|(?=[ ])|(?<=[ ])");
        return Arrays.stream(parts)
                .map(part -> PROVINCE_STATE_COUNTRY_CODES.contains(part.toUpperCase())
                        ? part.toUpperCase()
                        : COUNTRY_CODES_AND_NAMES.contains(part.toUpperCase())
                        ? formatCountry(part)
                        : part.matches("(?i)[A-Z]\\d[A-Z] ?\\d[A-Z]\\d|\\d{5}(-\\d{4})?")
                        ? formatPostalCode(part)
                        : capitalizeAddressWord(part))
                .reduce("", String::concat)
                .trim();
    }

    private static String capitalizeAddressWord(String word) {
        if (word.isBlank() || word.matches("\\d+")) {
            return word;
        }
        return capitalizeWord(word);
    }

    // Format Cities
    public static String formatCityName(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        String[] parts = input.split("(?=[ -])|(?<=[ -])");
        return Arrays.stream(parts)
                .map(StringFormatter::capitalizeWord)
                .reduce("", String::concat)
                .trim();
    }

    // Format Postal Codes
    public static String formatPostalCode(String input) {
        if (input.matches("(?i)[A-Z]\\d[A-Z] ?\\d[A-Z]\\d")) {
            return input.toUpperCase().replaceAll("(\\w{3})(\\w{3})", "$1 $2");
        }
        if (input.matches("\\d{5}(-\\d{4})?")) {
            return input;
        }
        return input;
    }

    // Format Countries
    public static String formatCountry(String input) {
        if (COUNTRY_CODES_AND_NAMES.contains(input.toUpperCase())) {
            return input.toUpperCase();
        }
        for (String country : COUNTRY_CODES_AND_NAMES) {
            if (country.equalsIgnoreCase(input)) {
                return capitalizeWord(country);
            }
        }
        return input;
    }

    private static String capitalizeWord(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }
}
