package com.example.util;

import java.util.Arrays;
import java.util.Set;

public class ClientNameFormatter {

    // Predefined list of special characters
    private static final Set<Character> SPECIAL_CHARACTERS = Set.of(
            '\'', '-', '/', '+', '&', '(', ')', '[', ']', '"', ',', '.', '!'
    );

    // Special Name Prefixes
    private static final String[] NAME_PREFIXES = {
            "Mc", "Mac", "O'", "D'", "De", "Van", "Von", "Le", "La", "Da", "Di", "Du", "Del",
            "De La", "Van Der", "De Los", "St.", "San", "Dos"
    };

    /**
     * Format the first name.
     *
     * @param firstName the raw first name
     * @return the formatted first name
     */
    public static String formatFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            return firstName;
        }
        return formatName(firstName);
    }

    /**
     * Format the last name.
     *
     * @param lastName the raw last name
     * @return the formatted last name
     */
    public static String formatLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return lastName;
        }
        return formatName(lastName);
    }

    /**
     * Format a name based on the rules for capitalization and special characters.
     *
     * @param name the raw name
     * @return the formatted name
     */
    private static String formatName(String name) {
        String[] parts = name.split("\\s+");
        return Arrays.stream(parts)
                .map(ClientNameFormatter::capitalizeNamePart)
                .reduce("", (a, b) -> a + " " + b).trim();
    }

    /**
     * Capitalize a single name part.
     *
     * @param part the raw name part
     * @return the formatted name part
     */
    private static String capitalizeNamePart(String part) {
        if (part.isBlank()) {
            return part;
        }

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
     * @param part the raw name part
     * @return the formatted name part
     */
    private static String capitalizeRest(String part) {
        StringBuilder formatted = new StringBuilder();
        boolean capitalizeNext = true; // Start by capitalizing the first letter

        for (char c : part.toCharArray()) {
            if (Character.isLetter(c)) {
                formatted.append(capitalizeNext ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNext = false; // Reset after processing a letter
            } else if (SPECIAL_CHARACTERS.contains(c)) {
                formatted.append(c); // Append special characters as-is
                capitalizeNext = true; // Capitalize next letter after a special character
            } else if (Character.isDigit(c)) {
                formatted.append(c); // Preserve numbers
            } else {
                formatted.append(c); // Append any other characters as-is
            }
        }

        return formatted.toString().replaceAll("\\s+", " ").trim(); // Normalize spaces
    }
}
