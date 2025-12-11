package com.marketplace.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Customer customer = new Customer();

        // Assert
        assertNotNull(customer);
        assertNull(customer.getId());
        assertNull(customer.getEmail());
        assertNull(customer.getCardNumber());
        assertNull(customer.getCardExpiryDate());
        assertNull(customer.getCardCvv());
        assertEquals(BigDecimal.ZERO, customer.getBalance());
    }

    @Test
    void testSetAndGetEmail() {
        // Arrange
        Customer customer = new Customer();
        String email = "customer@example.com";

        // Act
        customer.setEmail(email);

        // Assert
        assertEquals(email, customer.getEmail());
    }

    @Test
    void testSetAndGetCardNumber() {
        // Arrange
        Customer customer = new Customer();
        String cardNumber = "1234567812345678";

        // Act
        customer.setCardNumber(cardNumber);

        // Assert
        assertEquals(cardNumber, customer.getCardNumber());
    }

    @Test
    void testSetAndGetCardExpiryDate() {
        // Arrange
        Customer customer = new Customer();
        String cardExpiryDate = "12/25";

        // Act
        customer.setCardExpiryDate(cardExpiryDate);

        // Assert
        assertEquals(cardExpiryDate, customer.getCardExpiryDate());
    }

    @Test
    void testSetAndGetCardCvv() {
        // Arrange
        Customer customer = new Customer();
        String cardCvv = "123";

        // Act
        customer.setCardCvv(cardCvv);

        // Assert
        assertEquals(cardCvv, customer.getCardCvv());
    }

    @Test
    void testSetAndGetBalance() {
        // Arrange
        Customer customer = new Customer();
        BigDecimal balance = new BigDecimal("1500.00");

        // Act
        customer.setBalance(balance);

        // Assert
        assertEquals(balance, customer.getBalance());
    }