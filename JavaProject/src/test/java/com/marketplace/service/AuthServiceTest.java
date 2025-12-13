package com.marketplace.service;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.User;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder passwordEncoder;
    private Customer testCustomer;
    private Merchant testMerchant;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        // Setup test customer
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setEmail("customer@test.com");
        testCustomer.setPassword(passwordEncoder.encode("password123"));
        testCustomer.setName("Test Customer");
        testCustomer.setType(User.UserType.CUSTOMER);

        // Setup test merchant
        testMerchant = new Merchant();
        testMerchant.setId(2L);
        testMerchant.setEmail("merchant@test.com");
        testMerchant.setPassword(passwordEncoder.encode("password123"));
        testMerchant.setName("Test Merchant");
        testMerchant.setType(User.UserType.MERCHANT);
    }

    @Test
    void loginByEmail_WithValidCustomerCredentials_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findByEmail("customer@test.com"))
                .thenReturn(Optional.of(testCustomer));

        // Act
        User result = authService.loginByEmail("customer@test.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(User.UserType.CUSTOMER, result.getType());
        verify(customerRepository).findByEmail("customer@test.com");
    }

    @Test
    void loginByEmail_WithValidMerchantCredentials_ShouldReturnMerchant() {
        // Arrange
        when(customerRepository.findByEmail("merchant@test.com"))
                .thenReturn(Optional.empty());
        when(merchantRepository.findByEmail("merchant@test.com"))
                .thenReturn(Optional.of(testMerchant));

        // Act
        User result = authService.loginByEmail("merchant@test.com", "password123");

        // Assert
        assertNotNull(result);
        assertEquals(testMerchant.getId(), result.getId());
        assertEquals(User.UserType.MERCHANT, result.getType());
        verify(merchantRepository).findByEmail("merchant@test.com");
    }

    @Test
    void loginByEmail_WithInvalidEmail_ShouldThrowException() {
        // Arrange
        when(customerRepository.findByEmail("invalid@test.com"))
                .thenReturn(Optional.empty());
        when(merchantRepository.findByEmail("invalid@test.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.loginByEmail("invalid@test.com", "password123");
        });

        assertEquals("Yanlış email və ya şifrə", exception.getMessage());
    }

    @Test
    void loginByEmail_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        when(customerRepository.findByEmail("customer@test.com"))
                .thenReturn(Optional.of(testCustomer));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.loginByEmail("customer@test.com", "wrongpassword");
        });

        assertEquals("Yanlış email və ya şifrə", exception.getMessage());
    }

    @Test
    void registerMerchant_ShouldSaveMerchantWithEncodedPassword() {
        // Arrange
        Merchant newMerchant = new Merchant();
        newMerchant.setEmail("newmerchant@test.com");
        newMerchant.setPassword("plainPassword");
        newMerchant.setName("New Merchant");

        when(merchantRepository.save(any(Merchant.class))).thenReturn(newMerchant);

        // Act
        Merchant result = authService.registerMerchant(newMerchant);

        // Assert
        assertNotNull(result);
        assertEquals(User.UserType.MERCHANT, result.getType());
        assertNotEquals("plainPassword", result.getPassword());
        verify(merchantRepository).save(newMerchant);
    }

    @Test
    void registerCustomer_ShouldSaveCustomerWithEncodedPasswordAndInitialBalance() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setEmail("newcustomer@test.com");
        newCustomer.setPassword("plainPassword");
        newCustomer.setName("New Customer");

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        // Act
        Customer result = authService.registerCustomer(newCustomer);

        // Assert
        assertNotNull(result);
        assertEquals(User.UserType.CUSTOMER, result.getType());
        assertNotEquals("plainPassword", result.getPassword());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        verify(customerRepository).save(newCustomer);
    }
}
