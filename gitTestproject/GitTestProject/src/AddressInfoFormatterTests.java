
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressInfoFormatterTests {

    @Test
    void testBasicStreetAddress() {
        assertEquals("123 Main Street", AddressFormatter.formatAddress("123 MAIN STREET"));
        assertEquals("555 Arbour-Hall Road", AddressFormatter.formatAddress("555 ARBOUR-HALL ROAD"));
    }

    @Test
    void testUppercaseWords() {
        assertEquals("PO Box 567", AddressFormatter.formatAddress("PO BOX 567"));
        assertEquals("Suite 123, Floor 2", AddressFormatter.formatAddress("SUITE 123, FLOOR 2"));
    }

    @Test
    void testCanadianPostalCode() {
        assertEquals("H1M 2J5", AddressFormatter.formatAddress("H1M2J5"));
        assertEquals("A1A 1A1", AddressFormatter.formatAddress("A1A1A1"));
    }

    @Test
    void testUSZipCode() {
        assertEquals("12345", AddressFormatter.formatAddress("12345"));
        assertEquals("12345-6789", AddressFormatter.formatAddress("12345-6789"));
    }

    @Test
    void testHyphenatedAndApostropheWords() {
        assertEquals("O'Shaunessy Avenue", AddressFormatter.formatAddress("O'SHAUNESSY AVENUE"));
        assertEquals("Arbour-Hall Road", AddressFormatter.formatAddress("ARBOUR-HALL ROAD"));
    }

    @Test
    void testComplexAddress() {
        assertEquals("123 Main Street, PO Box 567, Ottawa-Gatineau, H1M 2J5",
                AddressFormatter.formatAddress("123 MAIN STREET, PO BOX 567, OTTAWA-GATINEAU, H1M2J5"));
    }
}
