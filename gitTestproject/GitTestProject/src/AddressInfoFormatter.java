package com.example.util;

import java.util.*;
import java.util.regex.Pattern;

public class AddressInfoFormatter {

    // Words to remain fully uppercase
    private static final Set<String> UPPERCASE_WORDS = Set.of("PO", "BOX", "APT", "SUITE", "FL", "FLOOR");

    // Canadian province abbreviations
    private static final Set<String> CANADIAN_PROVINCES = Set.of(
            "AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YT"
    );

    // US state abbreviations
    private static final Set<String> US_STATES = Set.of(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN",
            "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
            "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN",
            "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
    );

    // Country codes
    private static final Set<String> COUNTRY_CODES = Set.of("CA", "US");

    // Regex patterns
    private static final Pattern CANADIAN_POSTAL_CODE_REGEX = Pattern.compile("^[A-Z]\\d[A-Z] ?\\d[A-Z]\\d$");
    private static final Pattern US_ZIP_CODE_REGEX = Pattern.compile("^\\d{5}(-\\d{4})?$");
    private static final Pattern HYPHENATED_WORD_REGEX = Pattern.compile(".*-.*");
    private static final Pattern APOSTROPHE_WORD_REGEX = Pattern.compile(".*'.*");

    /**
     * Format an entire address string into properly formatted components.
     *
     * @param address the raw address string
     * @return the formatted address string
     */
    public static String formatAddress(String address) {
        if (address == null || address.isBlank()) {
            return address;
        }

        // Split address into logical parts based on commas
        String[] parts = address.split("\\s*,\\s*");
        StringBuilder formattedAddress = new StringBuilder();

        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                formattedAddress.append(processAddressPart(part)).append(", ");
            }
        }

        // Remove trailing comma and space
        return formattedAddress.toString().replaceAll(",\\s*$", "");
    }

    /**
     * Process a single part of the address.
     */
    private static String processAddressPart(String part) {
        if (CANADIAN_POSTAL_CODE_REGEX.matcher(part).matches()) {
            return formatCanadianPostalCode(part);
        } else if (US_ZIP_CODE_REGEX.matcher(part).matches()) {
            return part;
        } else if (CANADIAN_PROVINCES.contains(part.toUpperCase()) || US_STATES.contains(part.toUpperCase())) {
            return part.toUpperCase();
        } else if (COUNTRY_CODES.contains(part.toUpperCase())) {
            return part.toUpperCase();
        }

        String[] words = part.split("\\s+");
        StringBuilder processedPart = new StringBuilder();

        for (String word : words) {
            if (UPPERCASE_WORDS.contains(word.toUpperCase())) {
                processedPart.append(word.toUpperCase()).append(" ");
            } else if (HYPHENATED_WORD_REGEX.matcher(word).matches()) {
                processedPart.append(capitalizeHyphenatedWord(word)).append(" ");
            } else if (APOSTROPHE_WORD_REGEX.matcher(word).matches()) {
                processedPart.append(capitalizeApostropheWord(word)).append(" ");
            } else {
                processedPart.append(capitalizeWord(word)).append(" ");
            }
        }

        return processedPart.toString().trim();
    }

    /**
     * Format Canadian postal codes to 'A1A 1A1' format.
     */
    private static String formatCanadianPostalCode(String postalCode) {
        postalCode = postalCode.replaceAll("\\s+", "").toUpperCase();
        return postalCode.substring(0, 3) + " " + postalCode.substring(3);
    }

    /**
     * Capitalize hyphenated words (e.g., ARBOUR-HALL -> Arbour-Hall).
     */
    private static String capitalizeHyphenatedWord(String word) {
        String[] parts = word.split("-");
        return Arrays.stream(parts)
                .map(AddressFormatter::capitalizeWord)
                .reduce((a, b) -> a + "-" + b)
                .orElse(word);
    }

    /**
     * Capitalize words with apostrophes (e.g., O'SULLIVAN -> O'Sullivan).
     */
    private static String capitalizeApostropheWord(String word) {
        String[] parts = word.split("'");
        return Arrays.stream(parts)
                .map(AddressFormatter::capitalizeWord)
                .reduce((a, b) -> a + "'" + b)
                .orElse(word);
    }

    /**
     * Capitalize a single word (e.g., MAIN -> Main).
     */
    private static String capitalizeWord(String word) {
        if (word.isBlank()) return word;
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }
}
