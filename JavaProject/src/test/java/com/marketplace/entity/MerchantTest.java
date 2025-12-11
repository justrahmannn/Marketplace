package com.marketplace.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




class MerchantTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Merchant merchant = new Merchant();

        // Assert
        assertNotNull(merchant);
        assertNull(merchant.getEmail());
        assertNull(merchant.getCompanyName());
    }

    @Test
    void testSetAndGetEmail() {
        // Arrange
        Merchant merchant = new Merchant();
        String email = "merchant@example.com";

        // Act
        merchant.setEmail(email);

        // Assert
        assertEquals(email, merchant.getEmail());
    }

    @Test
    void testSetAndGetCompanyName() {
        // Arrange
        Merchant merchant = new Merchant();
        String companyName = "Test Company";

        // Act
        merchant.setCompanyName(companyName);

        // Assert
        assertEquals(companyName, merchant.getCompanyName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Merchant merchant1 = new Merchant();
        merchant1.setEmail("merchant@example.com");
        merchant1.setCompanyName("Company A");

        Merchant merchant2 = new Merchant();
        merchant2.setEmail("merchant@example.com");
        merchant2.setCompanyName("Company A");

        Merchant merchant3 = new Merchant();
        merchant3.setEmail("othermerchant@example.com");
        merchant3.setCompanyName("Company B");

        // Act & Assert
        assertEquals(merchant1, merchant2);
        assertNotEquals(merchant1, merchant3);
        assertEquals(merchant1.hashCode(), merchant2.hashCode());
        assertNotEquals(merchant1.hashCode(), merchant3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setEmail("merchant@example.com");
        merchant.setCompanyName("Test Company");

        // Act
        String result = merchant.toString();

        // Assert
        assertTrue(result.contains("email=merchant@example.com"));
        assertTrue(result.contains("companyName=Test Company"));
    }
}