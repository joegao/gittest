package com.example.util;

import java.util.Arrays;
import java.util.Set;

public class StringFormatter {

    // Canadian Provinces and Territories
    private static final Set<String> CANADIAN_PROVINCES = Set.of(
            "AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YT"
    );

    // U.S. States and Territories
    private static final Set<String> US_STATES = Set.of(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL",
            "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT",
            "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
            "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", "DC"
    );

    // Country Codes
    private static final Set<String> COUNTRY_CODES = Set.of(
            "US", "CA", "GB", "FR", "DE", "IT", "ES", "AU", "JP", "CN", "IN", "RU", "ZA",
            "USA", "CAN", "GBR", "FRA", "DEU", "ITA", "ESP", "AUS", "JPN", "CHN", "IND",
            "RUS", "ZAF"
    );

    // Full Country Names
    private static final Set<String> COUNTRY_NAMES = Set.of(
            "United States", "United States of America", "Canada", "United Kingdom",
            "France", "Germany", "Italy", "Spain", "Australia", "Japan", "China",
            "India", "Russia", "South Africa"
    );

    // Special Name Prefixes
    private static final Set<String> NAME_PREFIXES = Set.of(
            "Mc", "Mac", "O'", "D'", "De", "Van", "Von", "Le", "La", "Da", "Di", "Du", "Del",
            "De La", "Van Der", "De Los", "St.", "San", "Dos"
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
                .map(part -> CANADIAN_PROVINCES.contains(part.toUpperCase())
                        ? part.toUpperCase() // Canadian Provinces
                        : US_STATES.contains(part.toUpperCase())
                        ? part.toUpperCase() // US States
                        : COUNTRY_CODES.contains(part.toUpperCase())
                        ? formatCountry(part) // Countries
                        : part.matches("(?i)[A-Z]\\d[A-Z] ?\\d[A-Z]\\d|\\d{5}(-\\d{4})?")
                        ? formatPostalCode(part) // Postal Codes
                        : capitalizeAddressWord(part)) // Default
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
        if (COUNTRY_CODES.contains(input.toUpperCase())) {
            return input.toUpperCase();
        }
        for (String country : COUNTRY_NAMES) {
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
