package com.marketplace.service;

import com.marketplace.entity.*;
import com.marketplace.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private Product testProduct;
    private Cart testCart;
    private Category testCategory;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        // Setup test customer
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setEmail("customer@test.com");
        testCustomer.setName("Test Customer");
        testCustomer.setBalance(new BigDecimal("1000.00"));

        // Setup test category
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");

        // Setup test brand
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Samsung");

        // Setup test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setStockCount(10);
        testProduct.setCategory(testCategory);
        testProduct.setBrand(testBrand);

        // Setup test cart
        testCart = new Cart();
        testCart.setId(1L);
        testCart.setCustomer(testCustomer);
        testCart.setItems(new ArrayList<>());
    }

    @Test
    void getCart_WhenCartExists_ShouldReturnCart() {
        // Arrange
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));

        // Act
        Cart result = customerService.getCart(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCart.getId(), result.getId());
        verify(cartRepository).findByCustomerId(1L);
    }

    @Test
    void getCart_WhenCartDoesNotExist_ShouldCreateNewCart() {
        // Arrange
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.empty());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        Cart result = customerService.getCart(1L);

        // Assert
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addToCart_WithNewProduct_ShouldCreateNewCartItem() {
        // Arrange
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(new CartItem());

        // Act
        customerService.addToCart(1L, 1L, 2);

        // Assert
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addToCart_WithExistingProduct_ShouldUpdateQuantity() {
        // Arrange
        CartItem existingItem = new CartItem();
        existingItem.setId(1L);
        existingItem.setProduct(testProduct);
        existingItem.setCount(2);
        testCart.getItems().add(existingItem);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(existingItem);

        // Act
        customerService.addToCart(1L, 1L, 3);

        // Assert
        verify(cartItemRepository).save(argThat(item -> item.getCount() == 5));
    }

    @Test
    void makeOrder_WithSufficientBalanceAndStock_ShouldCreateOrder() {
        // Arrange
        CartItem cartItem = new CartItem();
        cartItem.setProduct(testProduct);
        cartItem.setCount(2);
        testCart.getItems().add(cartItem);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        customerService.makeOrder(1L);

        // Assert
        verify(orderRepository).save(any(Order.class));
        verify(productRepository).save(argThat(p -> p.getStockCount() == 8));
        verify(customerRepository).save(argThat(c -> c.getBalance().compareTo(new BigDecimal("800.00")) == 0));
    }

    @Test
    void makeOrder_WithInsufficientBalance_ShouldThrowException() {
        // Arrange
        testCustomer.setBalance(new BigDecimal("50.00"));
        CartItem cartItem = new CartItem();
        cartItem.setProduct(testProduct);
        cartItem.setCount(2);
        testCart.getItems().add(cartItem);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.makeOrder(1L);
        });

        assertTrue(exception.getMessage().contains("Balans kifayət deyil"));
    }

    @Test
    void makeOrder_WithInsufficientStock_ShouldThrowException() {
        // Arrange
        testProduct.setStockCount(1);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(testProduct);
        cartItem.setCount(5);
        testCart.getItems().add(cartItem);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.makeOrder(1L);
        });

        assertTrue(exception.getMessage().contains("stok yoxdur"));
    }

    @Test
    void makeOrder_WithEmptyCart_ShouldThrowException() {
        // Arrange
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.makeOrder(1L);
        });

        assertEquals("Səbət boşdur", exception.getMessage());
    }

    @Test
    void cancelOrder_WithValidOrder_ShouldRefundAndRestoreStock() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setCustomer(testCustomer);
        order.setProduct(testProduct);
        order.setCount(2);
        order.setTotalAmount(new BigDecimal("200.00"));
        order.setStatus(Order.OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        customerService.cancelOrder(1L);

        // Assert
        verify(customerRepository).save(argThat(c -> c.getBalance().compareTo(new BigDecimal("1200.00")) == 0));
        verify(productRepository).save(argThat(p -> p.getStockCount() == 12));
        verify(orderRepository).save(argThat(o -> o.getStatus() == Order.OrderStatus.REJECT_BY_CUSTOMER));
    }

    @Test
    void addToWishlist_ShouldSaveWishlistItem() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(new Wishlist());

        // Act
        customerService.addToWishlist(1L, 1L);

        // Assert
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void getAllProducts_ShouldReturnOnlyInStockProducts() {
        // Arrange
        Product outOfStockProduct = new Product();
        outOfStockProduct.setId(2L);
        outOfStockProduct.setStockCount(0);

        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct, outOfStockProduct));

        // Act
        List<Product> result = customerService.getAllProducts();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testProduct.getId(), result.get(0).getId());
    }

    @Test
    void searchProducts_WithQuery_ShouldReturnMatchingProducts() {
        // Arrange
        when(productRepository.findByNameContainingIgnoreCase("Test"))
                .thenReturn(Arrays.asList(testProduct));

        // Act
        List<Product> result = customerService.searchProducts("Test");

        // Assert
        assertEquals(1, result.size());
        verify(productRepository).findByNameContainingIgnoreCase("Test");
    }

    @Test
    void increaseBalance_WithValidCard_ShouldIncreaseBalance() {
        // Arrange
        testCustomer.setCardNumber("1234567890123456");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        customerService.increaseBalance(1L, new BigDecimal("500.00"));

        // Assert
        verify(customerRepository).save(argThat(c -> c.getBalance().compareTo(new BigDecimal("1500.00")) == 0));
    }

    @Test
    void increaseBalance_WithoutCard_ShouldThrowException() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.increaseBalance(1L, new BigDecimal("500.00"));
        });

        assertTrue(exception.getMessage().contains("bank kartı"));
    }

    @Test
    void getCartTotal_ShouldCalculateCorrectTotal() {
        // Arrange
        CartItem item1 = new CartItem();
        item1.setProduct(testProduct);
        item1.setCount(2);

        Product product2 = new Product();
        product2.setPrice(new BigDecimal("50.00"));
        CartItem item2 = new CartItem();
        item2.setProduct(product2);
        item2.setCount(3);

        testCart.getItems().addAll(Arrays.asList(item1, item2));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(testCart));

        // Act
        BigDecimal total = customerService.getCartTotal(1L);

        // Assert
        assertEquals(new BigDecimal("350.00"), total);
    }
}
