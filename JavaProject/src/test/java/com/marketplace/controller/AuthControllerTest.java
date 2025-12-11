package com.marketplace.controller;
import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.User;
import com.marketplace.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/controller/AuthControllerTest.java



class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        String result = authController.index();
        assertEquals("redirect:/", result);
    }

    @Test
    void testCustomerLoginPage() {
        String result = authController.customerLoginPage();
        assertEquals("customer_login", result);
    }

    @Test
    void testCustomerLogin_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setType(User.UserType.CUSTOMER);

        when(authService.loginByEmail("test@example.com", "password")).thenReturn(user);

        String result = authController.customerLogin("test@example.com", "password", model);

        assertEquals("redirect:/customer/products?userId=1", result);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testCustomerLogin_WrongUserType() throws Exception {
        User user = new User();
        user.setType(User.UserType.MERCHANT);

        when(authService.loginByEmail("test@example.com", "password")).thenReturn(user);

        String result = authController.customerLogin("test@example.com", "password", model);

        assertEquals("customer_login", result);
        verify(model).addAttribute("error", "Bu hesab alıcı hesabı deyil");
    }

    @Test
    void testCustomerLogin_Exception() throws Exception {
        when(authService.loginByEmail("test@example.com", "password")).thenThrow(new RuntimeException("Login failed"));

        String result = authController.customerLogin("test@example.com", "password", model);

        assertEquals("customer_login", result);
        verify(model).addAttribute("error", "Login failed");
    }

    @Test
    void testCustomerRegisterPage() {
        String result = authController.customerRegisterPage();
        assertEquals("customer_register", result);
    }

    @Test
    void testRegisterCustomer() {
        String result = authController.registerCustomer("John", "Doe", "john@example.com", "password");

        assertEquals("redirect:/auth/customer/login", result);
        verify(authService).registerCustomer(any(Customer.class));
    }

    @Test
    void testMerchantLoginPage() {
        String result = authController.merchantLoginPage();
        assertEquals("merchant_login", result);
    }

    @Test
    void testMerchantLogin_Success() throws Exception {
        User user = new User();
        user.setId(2L);
        user.setType(User.UserType.MERCHANT);

        when(authService.loginByEmail("merchant@example.com", "password")).thenReturn(user);

        String result = authController.merchantLogin("merchant@example.com", "password", model);

        assertEquals("redirect:/merchant/dashboard?merchantId=2", result);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testMerchantLogin_WrongUserType() throws Exception {
        User user = new User();
        user.setType(User.UserType.CUSTOMER);

        when(authService.loginByEmail("merchant@example.com", "password")).thenReturn(user);

        String result = authController.merchantLogin("merchant@example.com", "password", model);

        assertEquals("merchant_login", result);
        verify(model).addAttribute("error", "Bu hesab satıcı hesabı deyil");
    }

    @Test
    void testMerchantLogin_Exception() throws Exception {
        when(authService.loginByEmail("merchant@example.com", "password")).thenThrow(new RuntimeException("Login failed"));

        String result = authController.merchantLogin("merchant@example.com", "password", model);

        assertEquals("merchant_login", result);
        verify(model).addAttribute("error", "Login failed");
    }

    @Test
    void testMerchantRegisterPage() {
        String result = authController.merchantRegisterPage();
        assertEquals("merchant_register", result);
    }

    @Test
    void testRegisterMerchant() {
        String result = authController.registerMerchant("Jane", "Doe", "jane@example.com", "password", "Company");

        assertEquals("redirect:/auth/merchant/login", result);
        verify(authService).registerMerchant(any(Merchant.class));
    }

    @Test
    void testLogout() {
        String result = authController.logout();
        assertEquals("redirect:/", result);
    }
}