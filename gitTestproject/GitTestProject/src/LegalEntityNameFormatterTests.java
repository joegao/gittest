package com.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegalEntityNameFormatterTests {

    @Test
    void testBusinessDesignatorsAreUppercase() {
        // Basic business designators
        assertEquals("ACME INC.", LegalEntityNameFormatter.formatLegalEntityName("ACME INC."));
        assertEquals("OMEGA CORP.", LegalEntityNameFormatter.formatLegalEntityName("OMEGA CORP."));
        assertEquals("ALPHA & BETA LTD.", LegalEntityNameFormatter.formatLegalEntityName("ALPHA & BETA LTD."));
        assertEquals("DELTA HOLDINGS LLC", LegalEntityNameFormatter.formatLegalEntityName("DELTA HOLDINGS LLC"));
    }

    @Test
    void testNameWithPrefixes() {
        // Legal entity names with prefixes
        assertEquals("McDonald's Corp.", LegalEntityNameFormatter.formatLegalEntityName("MCDONALD'S CORP."));
        assertEquals("De La Cruz Inc.", LegalEntityNameFormatter.formatLegalEntityName("DE LA CRUZ INC."));
        assertEquals("Van Der Meer LLC", LegalEntityNameFormatter.formatLegalEntityName("VAN DER MEER LLC"));
        assertEquals("O'Sullivan & Partners", LegalEntityNameFormatter.formatLegalEntityName("O'SULLIVAN & PARTNERS"));

        // Mixed prefixes and designators
        assertEquals("MacLeod Group Inc.", LegalEntityNameFormatter.formatLegalEntityName("MACLEOD GROUP INC."));
        assertEquals("Van Gogh Arts Ltd.", LegalEntityNameFormatter.formatLegalEntityName("VAN GOGH ARTS LTD."));
    }

    @Test
    void testSpecialCharactersInNames() {
        // Names with special characters
        assertEquals("D'Arcy Consulting LLC", LegalEntityNameFormatter.formatLegalEntityName("D'ARCY CONSULTING LLC"));
        assertEquals("St. John's Enterprises", LegalEntityNameFormatter.formatLegalEntityName("ST. JOHN'S ENTERPRISES"));
        assertEquals("Alpha & Beta Co.", LegalEntityNameFormatter.formatLegalEntityName("ALPHA & BETA CO."));
        assertEquals("Delta+Gamma Partners", LegalEntityNameFormatter.formatLegalEntityName("DELTA+GAMMA PARTNERS"));
        assertEquals("Smith/Jones Group", LegalEntityNameFormatter.formatLegalEntityName("SMITH/JONES GROUP"));
    }

    @Test
    void testDiacriticalMarksInNames() {
        // Names with diacritical marks
        assertEquals("Café Renée's", LegalEntityNameFormatter.formatLegalEntityName("CAFÉ RENÉE'S"));
        assertEquals("Peña's Enterprises", LegalEntityNameFormatter.formatLegalEntityName("PEÑA'S ENTERPRISES"));
        assertEquals("François & Co.", LegalEntityNameFormatter.formatLegalEntityName("FRANÇOIS & CO."));
        assertEquals("Müller Group GmbH", LegalEntityNameFormatter.formatLegalEntityName("MÜLLER GROUP GMBH"));
    }

    @Test
    void testFrenchBusinessDesignators() {
        // French business designators
        assertEquals("SARL Dupont Et Fils", LegalEntityNameFormatter.formatLegalEntityName("SARL DUPONT ET FILS"));
        assertEquals("EURL Martin", LegalEntityNameFormatter.formatLegalEntityName("EURL MARTIN"));
        assertEquals("SAS Peugeot", LegalEntityNameFormatter.formatLegalEntityName("SAS PEUGEOT"));
        assertEquals("SNC Boulanger Et Associés", LegalEntityNameFormatter.formatLegalEntityName("SNC BOULANGER ET ASSOCIÉS"));
        assertEquals("GIE Agricole", LegalEntityNameFormatter.formatLegalEntityName("GIE AGRICOLE"));
    }

    @Test
    void testCommonLowercaseWords() {
        // Names with common lowercase words
        assertEquals("The Office of Technology", LegalEntityNameFormatter.formatLegalEntityName("THE OFFICE OF TECHNOLOGY"));
        assertEquals("Group for Research and Development", LegalEntityNameFormatter.formatLegalEntityName("GROUP FOR RESEARCH AND DEVELOPMENT"));
        assertEquals("Academy of Arts and Sciences", LegalEntityNameFormatter.formatLegalEntityName("ACADEMY OF ARTS AND SCIENCES"));
    }

    @Test
    void testExcessiveSpacesAndNormalization() {
        // Names with excessive spaces
        assertEquals("Acme Corp.", LegalEntityNameFormatter.formatLegalEntityName("  ACME   CORP.  "));
        assertEquals("John's Café, Ltd.", LegalEntityNameFormatter.formatLegalEntityName("JOHN'S CAFÉ, LTD."));
        assertEquals("Smith & Sons, Inc.", LegalEntityNameFormatter.formatLegalEntityName("  SMITH   &   SONS,   INC.  "));
    }

    @Test
    void testMixedCasesAndComplexNames() {
        // Complex legal entity names
        assertEquals("Van Gogh Arts & Sciences Inc.", LegalEntityNameFormatter.formatLegalEntityName("VAN GOGH ARTS & SCIENCES INC."));
        assertEquals("McDonald's, St. John's, & Partners LLC", LegalEntityNameFormatter.formatLegalEntityName("MCDONALD'S, ST. JOHN'S, & PARTNERS LLC"));
        assertEquals("Delta+Gamma/Rho Enterprises Corp.", LegalEntityNameFormatter.formatLegalEntityName("DELTA+GAMMA/RHO ENTERPRISES CORP."));
    }
}
