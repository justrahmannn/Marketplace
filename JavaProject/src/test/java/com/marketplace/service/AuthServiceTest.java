import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.User;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/service/AuthServiceTest.java
package com.marketplace.service;




class AuthServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginByEmail_CustomerSuccess() {
        Customer customer = new Customer();
        customer.setEmail("customer@example.com");
        customer.setPassword("encodedPassword");
        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = authService.loginByEmail("customer@example.com", "password");

        assertEquals(customer, result);
        verify(customerRepository).findByEmail("customer@example.com");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(merchantRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLoginByEmail_MerchantSuccess() {
        Merchant merchant = new Merchant();
        merchant.setEmail("merchant@example.com");
        merchant.setPassword("encodedPassword");
        when(customerRepository.findByEmail("merchant@example.com")).thenReturn(Optional.empty());
        when(merchantRepository.findByEmail("merchant@example.com")).thenReturn(Optional.of(merchant));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User result = authService.loginByEmail("merchant@example.com", "password");

        assertEquals(merchant, result);
        verify(customerRepository).findByEmail("merchant@example.com");
        verify(merchantRepository).findByEmail("merchant@example.com");
        verify(passwordEncoder).matches("password", "encodedPassword");
    }

    @Test
    void testLoginByEmail_InvalidCredentials() {
        when(customerRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());
        when(merchantRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            authService.loginByEmail("invalid@example.com", "password")
        );

        assertEquals("Yanlış email və ya şifrə", exception.getMessage());
        verify(customerRepository).findByEmail("invalid@example.com");
        verify(merchantRepository).findByEmail("invalid@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testRegisterMerchant() {
        Merchant merchant = new Merchant();
        merchant.setPassword("plainPassword");
        Merchant savedMerchant = new Merchant();
        savedMerchant.setPassword("encodedPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(merchantRepository.save(merchant)).thenReturn(savedMerchant);

        Merchant result = authService.registerMerchant(merchant);

        assertEquals(savedMerchant, result);
        assertEquals(User.UserType.MERCHANT, merchant.getType());
        verify(passwordEncoder).encode("plainPassword");
        verify(merchantRepository).save(merchant);
    }

    @Test
    void testRegisterCustomer() {
        Customer customer = new Customer();
        customer.setPassword("plainPassword");
        Customer savedCustomer = new Customer();
        savedCustomer.setPassword("encodedPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        Customer result = authService.registerCustomer(customer);

        assertEquals(savedCustomer, result);
        assertEquals(User.UserType.CUSTOMER, customer.getType());
        assertEquals(new BigDecimal("1000.00"), customer.getBalance());
        verify(passwordEncoder).encode("plainPassword");
        verify(customerRepository).save(customer);
    }
}