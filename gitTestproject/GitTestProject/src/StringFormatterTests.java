package com.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringFormatterTests {

    @Test
    void testFormatName() {
        // Single-word names
        assertEquals("Catherine", StringFormatter.formatName("CATHERINE"));
        assertEquals("O'Shea", StringFormatter.formatName("O'SHEA"));

        // Multi-word names
        assertEquals("Catherine Marie", StringFormatter.formatName("CATHRINE MARIE"));
        assertEquals("Marie-Anne", StringFormatter.formatName("MARIE-ANNE"));

        // Prefixes
        assertEquals("McDonald", StringFormatter.formatName("MCDONALD"));
        assertEquals("MacLeod", StringFormatter.formatName("MACLEOD"));
        assertEquals("De La Cruz", StringFormatter.formatName("DE LA CRUZ"));
    }

    @Test
    void testSpecialCharactersInNamesWithPredefinedList() {
        // Apostrophes, Hyphens, and Slashes
        assertEquals("O'Connor-Lee", ClientNameFormatter.formatLastName("O'CONNOR-LEE"));
        assertEquals("Smith/Jones", ClientNameFormatter.formatLastName("SMITH/JONES"));

        // Plus and Ampersand
        assertEquals("John+Jane", ClientNameFormatter.formatFirstName("JOHN+JANE"));
        assertEquals("Mary & Anne", ClientNameFormatter.formatFirstName("MARY & ANNE"));

        // Parentheses, Brackets, and Quotation Marks
        assertEquals("John (The Brave)", ClientNameFormatter.formatLastName("JOHN (THE BRAVE)"));
        assertEquals("Smith [The Brave]", ClientNameFormatter.formatLastName("SMITH [THE BRAVE]"));
        assertEquals("John \"The Brave\" Smith", ClientNameFormatter.formatLastName("JOHN \"THE BRAVE\" SMITH"));

        // Numbers
        assertEquals("Henry IV", ClientNameFormatter.formatLastName("HENRY IV"));
        assertEquals("Agent47", ClientNameFormatter.formatFirstName("AGENT47"));

        // Periods and Commas
        assertEquals("Dr. John, Jr.", ClientNameFormatter.formatLastName("DR. JOHN, JR."));
    }

    @Test
    void testNameWithAccents() {
        // Accents with lowercase input
        assertEquals("Émilie", ClientNameFormatter.formatFirstName("émilie"));
        assertEquals("José", ClientNameFormatter.formatFirstName("josÉ"));
        assertEquals("François", ClientNameFormatter.formatLastName("franÇois"));

        // Accents with mixed-case input
        assertEquals("Peña", ClientNameFormatter.formatLastName("peÑA"));
        assertEquals("Gràcia", ClientNameFormatter.formatFirstName("grÀcia"));

        // Names with tilde and umlaut
        assertEquals("São", ClientNameFormatter.formatFirstName("sÃo"));
        assertEquals("Müller", ClientNameFormatter.formatLastName("mÜller"));

        // Standard names without accents
        assertEquals("Marie", ClientNameFormatter.formatFirstName("marie"));
        assertEquals("Jean-Paul", ClientNameFormatter.formatLastName("jean-paul"));
    }
2o

    @Test
    void testFormatAddress() {
        // General address examples
        assertEquals("123 Front Street, Apt 987, Po Box 500",
                StringFormatter.formatAddress("123 FRONT STREET, APT 987, PO BOX 500"));
        assertEquals("555 Arbour-Hall Road, Suite 123, Lower Hall",
                StringFormatter.formatAddress("555 ARBOUR-HALL ROAD, SUITE 123, LOWER HALL"));
        assertEquals("999 O'Shaunessy Ave, Floor 1, Second Door",
                StringFormatter.formatAddress("999 O'SHAUNESSY AVE, FLOOR 1, SECOND DOOR"));

        // Address with postal codes and countries
        assertEquals("123 Main Street, Toronto, ON, M5W 1E6, Canada",
                StringFormatter.formatAddress("123 MAIN STREET, TORONTO, ON, M5W1E6, CANADA"));
        assertEquals("456 Broadway Ave, New York, NY, 10001, USA",
                StringFormatter.formatAddress("456 BROADWAY AVE, NEW YORK, NY, 10001, USA"));
        assertEquals("789 Elm Street, Vancouver, BC, V6C 3N3",
                StringFormatter.formatAddress("789 ELM STREET, VANCOUVER, BC, V6C3N3"));
    }

    @Test
    void testFormatCityName() {
        // General city names
        assertEquals("Toronto", StringFormatter.formatCityName("Toronto"));
        assertEquals("Saint John", StringFormatter.formatCityName("SAINT JOHN"));
        assertEquals("Peggy's Cove", StringFormatter.formatCityName("PEGGY'S COVE"));
        assertEquals("Ottawa-Gatineau", StringFormatter.formatCityName("OTTAWA-GATINEAU"));

        // City names with prefixes or special cases
        assertEquals("La Paz", StringFormatter.formatCityName("LA PAZ"));
        assertEquals("San Francisco", StringFormatter.formatCityName("SAN FRANCISCO"));
    }

    @Test
    void testFormatPostalCode() {
        // Canadian postal codes
        assertEquals("M5W 1E6", StringFormatter.formatPostalCode("M5W1E6"));
        assertEquals("K1A 0B1", StringFormatter.formatPostalCode("k1a0b1"));
        assertEquals("V6C 3N3", StringFormatter.formatPostalCode("v6c3n3"));

        // US ZIP codes
        assertEquals("12345", StringFormatter.formatPostalCode("12345"));
        assertEquals("12345-6789", StringFormatter.formatPostalCode("12345-6789"));

        // Invalid postal codes (no formatting applied)
        assertEquals("ABC123", StringFormatter.formatPostalCode("ABC123"));
    }

    @Test
    void testFormatCountry() {
        // Abbreviations
        assertEquals("USA", StringFormatter.formatCountry("usa"));
        assertEquals("CAN", StringFormatter.formatCountry("can"));

        // Full names
        assertEquals("United States", StringFormatter.formatCountry("UNITED STATES"));
        assertEquals("Canada", StringFormatter.formatCountry("CANADA"));
        assertEquals("United Kingdom", StringFormatter.formatCountry("united kingdom"));
    }

    @Test
    void testCombinedFormatting() {
        // Full formatted address including city, postal code, and country
        String input = "456 BROADWAY AVE, NEW YORK, NY, 10001, USA";
        String expected = "456 Broadway Ave, New York, NY, 10001, USA";
        assertEquals(expected, StringFormatter.formatAddress(input));

        input = "123 MAIN STREET, TORONTO, ON, M5W1E6, CANADA";
        expected = "123 Main Street, Toronto, ON, M5W 1E6, Canada";
        assertEquals(expected, StringFormatter.formatAddress(input));
    }
}
