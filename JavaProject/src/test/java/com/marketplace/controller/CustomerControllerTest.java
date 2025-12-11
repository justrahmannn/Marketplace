package com.marketplace.controller;
import com.marketplace.entity.Product;
import com.marketplace.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/controller/CustomerControllerTest.java




class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts_NoFilters() {
        List<Product> products = new ArrayList<>();
        when(customerService.getAllProducts()).thenReturn(products);
        when(customerService.getAllCategories()).thenReturn(new ArrayList<>());
        when(customerService.getAllBrands()).thenReturn(new ArrayList<>());

        String result = customerController.listProducts(1L, null, null, null, model);

        assertEquals("products", result);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("categories", new ArrayList<>());
        verify(model).addAttribute("brands", new ArrayList<>());
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testListProducts_WithSearch() {
        List<Product> products = new ArrayList<>();
        when(customerService.searchProducts("test")).thenReturn(products);

        String result = customerController.listProducts(1L, "test", null, null, model);

        assertEquals("products", result);
        verify(model).addAttribute("products", products);
    }

    @Test
    void testViewProduct() {
        Product product = new Product();
        when(customerService.getProduct(1L)).thenReturn(product);

        String result = customerController.viewProduct(1L, 1L, model);

        assertEquals("product_detail", result);
        verify(model).addAttribute("product", product);
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testViewAccount() {
        when(customerService.getCustomer(1L)).thenReturn(new Object());
        when(customerService.getCustomerOrders(1L)).thenReturn(new ArrayList<>());

        String result = customerController.viewAccount(1L, model);

        assertEquals("account", result);
        verify(model).addAttribute("customer", new Object());
        verify(model).addAttribute("orders", new ArrayList<>());
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testAddToCart() {
        String result = customerController.addToCart(1L, 2L, 3);

        assertEquals("redirect:/customer/cart?userId=1", result);
        verify(customerService).addToCart(1L, 2L, 3);
    }

    @Test
    void testViewCart() {
        when(customerService.getCart(1L)).thenReturn(new ArrayList<>());
        when(customerService.getCartTotal(1L)).thenReturn(BigDecimal.TEN);

        String result = customerController.viewCart(1L, model);

        assertEquals("cart", result);
        verify(model).addAttribute("cart", new ArrayList<>());
        verify(model).addAttribute("totalAmount", BigDecimal.TEN);
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testRemoveFromCart() {
        String result = customerController.removeFromCart(1L, 2L);

        assertEquals("redirect:/customer/cart?userId=1", result);
        verify(customerService).removeFromCart(2L);
    }

    @Test
    void testMakeOrder_Success() {
        String result = customerController.makeOrder(1L, model);

        assertEquals("redirect:/customer/order-success?userId=1", result);
        verify(customerService).makeOrder(1L);
    }

    @Test
    void testMakeOrder_Failure() {
        when(customerService.makeOrder(1L)).thenThrow(new RuntimeException("Order failed"));
        when(customerService.getCart(1L)).thenReturn(new ArrayList<>());
        when(customerService.getCartTotal(1L)).thenReturn(BigDecimal.TEN);

        String result = customerController.makeOrder(1L, model);

        assertEquals("cart", result);
        verify(model).addAttribute("error", "Order failed");
        verify(model).addAttribute("cart", new ArrayList<>());
        verify(model).addAttribute("totalAmount", BigDecimal.TEN);
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testOrderSuccess() {
        when(customerService.getCustomer(1L)).thenReturn(new Object());
        when(customerService.getCustomerOrders(1L)).thenReturn(new ArrayList<>());

        String result = customerController.orderSuccess(1L, model);

        assertEquals("order_success", result);
        verify(model).addAttribute("userId", 1L);
        verify(model).addAttribute("customer", new Object());
        verify(model).addAttribute("orders", new ArrayList<>());
    }

    @Test
    void testIncreaseBalance_Success() {
        String result = customerController.increaseBalance(1L, BigDecimal.TEN, model);

        assertEquals("redirect:/customer/account?userId=1", result);
        verify(customerService).increaseBalance(1L, BigDecimal.TEN);
    }

    @Test
    void testIncreaseBalance_Failure() {
        when(customerService.increaseBalance(1L, BigDecimal.TEN)).thenThrow(new RuntimeException("Balance error"));
        when(customerService.getCustomer(1L)).thenReturn(new Object());
        when(customerService.getCustomerOrders(1L)).thenReturn(new ArrayList<>());

        String result = customerController.increaseBalance(1L, BigDecimal.TEN, model);

        assertEquals("account", result);
        verify(model).addAttribute("error", "Balance error");
        verify(model).addAttribute("customer", new Object());
        verify(model).addAttribute("orders", new ArrayList<>());
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testAddCard() {
        String result = customerController.addCard(1L, "1234", "12/23", "123");

        assertEquals("redirect:/customer/account?userId=1", result);
        verify(customerService).addCard(1L, "1234", "12/23", "123");
    }

    @Test
    void testCancelOrder() {
        String result = customerController.cancelOrder(1L, 2L);

        assertEquals("redirect:/customer/account?userId=2", result);
        verify(customerService).cancelOrder(1L);
    }

    @Test
    void testViewWishlist() {
        when(customerService.getWishlist(1L)).thenReturn(new ArrayList<>());

        String result = customerController.viewWishlist(1L, model);

        assertEquals("wishlist", result);
        verify(model).addAttribute("wishlist", new ArrayList<>());
        verify(model).addAttribute("userId", 1L);
    }

    @Test
    void testAddToWishlist() {
        String result = customerController.addToWishlist(1L, 2L);

        assertEquals("redirect:/customer/wishlist?userId=1", result);
        verify(customerService).addToWishlist(1L, 2L);
    }

    @Test
    void testRemoveFromWishlist() {
        String result = customerController.removeFromWishlist(1L, 2L);

        assertEquals("redirect:/customer/wishlist?userId=1", result);
        verify(customerService).removeFromWishlist(2L);
    }
}