package com.example.util;

import java.util.Arrays;
import java.util.Set;

public class LegalEntityNameFormatter {

    // List of common business designators
    private static final Set<String> BUSINESS_DESIGNATORS = Set.of(
            "LLC", "INC.", "LTD.", "CORP.", "CO.", "COMPANY", "GROUP", "PARTNERS", "PLC", "GMBH", "AG",
            "SARL", "SA", "EURL", "SNC", "SAS", "SASU", "GIE", "SCI", "EI"
    );

    // Predefined list of special characters to preserve
    private static final Set<Character> SPECIAL_CHARACTERS = Set.of(
            '\'', '-', '/', '+', '&', '(', ')', ',', '.'
    );

    // List of name prefixes
    private static final String[] NAME_PREFIXES = {
            "Mc", "Mac", "O'", "D'", "De", "Van", "Von", "Le", "La", "Da", "Di", "Du", "Del",
            "De La", "Van Der", "De Los", "St.", "San", "Dos"
    };

    // Common words in legal names that are always lowercase unless they start the name
    private static final Set<String> COMMON_LOWERCASE_WORDS = Set.of(
            "and", "of", "the", "in", "at", "on", "for", "by", "with"
    );

    /**
     * Format a legal entity name according to business conventions.
     *
     * @param entityName the raw legal entity name
     * @return the formatted legal entity name
     */
    public static String formatLegalEntityName(String entityName) {
        if (entityName == null || entityName.isBlank()) {
            return entityName;
        }

        String[] parts = entityName.split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String formattedPart;

            if (BUSINESS_DESIGNATORS.contains(part.toUpperCase())) {
                // Keep business designators in uppercase
                formattedPart = part.toUpperCase();
            } else if (COMMON_LOWERCASE_WORDS.contains(part.toLowerCase()) && i != 0) {
                // Lowercase common words unless they are the first word
                formattedPart = part.toLowerCase();
            } else {
                // Apply prefix-aware capitalization rules
                formattedPart = capitalizeWithPrefixes(part);
            }

            formattedName.append(formattedPart).append(" ");
        }

        return formattedName.toString().trim();
    }

    /**
     * Capitalize a word, considering name prefixes and special characters.
     *
     * @param part the raw word
     * @return the formatted word
     */
    private static String capitalizeWithPrefixes(String part) {
        for (String prefix : NAME_PREFIXES) {
            if (part.toUpperCase().startsWith(prefix.toUpperCase()) && part.length() > prefix.length()) {
                return prefix + capitalizeRest(part.substring(prefix.length()));
            }
        }

        return capitalizeRest(part);
    }

    /**
     * Capitalize the first letter and format the rest of the word, preserving special characters.
     *
     * @param part the raw word
     * @return the formatted word
     */
    private static String capitalizeRest(String part) {
        StringBuilder formatted = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : part.toCharArray()) {
            if (Character.isLetter(c)) {
                formatted.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNext = false; // Reset capitalization after a letter
            } else if (SPECIAL_CHARACTERS.contains(c)) {
                formatted.append(c); // Append special characters as-is
                capitalizeNext = true; // Capitalize next letter after a special character
            } else {
                formatted.append(c); // Append any other characters as-is
            }
        }

        return formatted.toString();
    }
}
