package com.marketplace.service;

import com.marketplace.entity.*;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private MerchantService merchantService;

    private Merchant testMerchant;
    private Product testProduct;
    private Customer testCustomer;
    private Order testOrder;
    private Category testCategory;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        // Setup test merchant
        testMerchant = new Merchant();
        testMerchant.setId(1L);
        testMerchant.setEmail("merchant@test.com");
        testMerchant.setName("Test Merchant");

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
        testProduct.setMerchant(testMerchant);
        testProduct.setCategory(testCategory);
        testProduct.setBrand(testBrand);

        // Setup test customer
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setBalance(new BigDecimal("500.00"));

        // Setup test order
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomer(testCustomer);
        testOrder.setProduct(testProduct);
        testOrder.setCount(2);
        testOrder.setTotalAmount(new BigDecimal("200.00"));
        testOrder.setStatus(Order.OrderStatus.CREATED);
    }

    @Test
    void createProduct_WithValidMerchant_ShouldSaveProduct() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(new BigDecimal("150.00"));

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(testMerchant));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // Act
        Product result = merchantService.createProduct(1L, newProduct, null);

        // Assert
        assertNotNull(result);
        assertEquals(testMerchant, newProduct.getMerchant());
        verify(productRepository).save(newProduct);
    }

    @Test
    void createProduct_WithInvalidMerchant_ShouldThrowException() {
        // Arrange
        when(merchantRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.createProduct(999L, new Product(), null);
        });

        assertTrue(exception.getMessage().contains("Merchant"));
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void updateProduct_WithValidProduct_ShouldUpdateAllFields() {
        // Arrange
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Product");
        updatedDetails.setDetails("Updated details");
        updatedDetails.setPrice(new BigDecimal("200.00"));
        updatedDetails.setStockCount(20);
        updatedDetails.setCategory(testCategory);
        updatedDetails.setBrand(testBrand);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = merchantService.updateProduct(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        verify(productRepository).save(argThat(p -> p.getName().equals("Updated Product") &&
                p.getPrice().compareTo(new BigDecimal("200.00")) == 0 &&
                p.getStockCount() == 20));
    }

    @Test
    void updateProduct_WithInvalidProductId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            merchantService.updateProduct(999L, new Product());
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void deleteProduct_ShouldCallRepository() {
        // Act
        merchantService.deleteProduct(1L);

        // Assert
        verify(productRepository).deleteById(1L);
    }

    @Test
    void getMerchantProducts_ShouldReturnProductList() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByMerchantId(1L)).thenReturn(products);

        // Act
        List<Product> result = merchantService.getMerchantProducts(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testProduct.getId(), result.get(0).getId());
        verify(productRepository).findByMerchantId(1L);
    }

    @Test
    void getMerchantOrders_ShouldReturnOrderList() {
        // Arrange
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByProductMerchantId(1L)).thenReturn(orders);

        // Act
        List<Order> result = merchantService.getMerchantOrders(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        verify(orderRepository).findByProductMerchantId(1L);
    }

    @Test
    void acceptOrder_ShouldUpdateOrderStatus() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order result = merchantService.acceptOrder(1L);

        // Assert
        verify(orderRepository).save(argThat(o -> o.getStatus() == Order.OrderStatus.ACCEPTED));
    }

    @Test
    void deliverOrder_ShouldUpdateOrderStatus() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order result = merchantService.deliverOrder(1L);

        // Assert
        verify(orderRepository).save(argThat(o -> o.getStatus() == Order.OrderStatus.DELIVERED));
    }

    @Test
    void rejectOrder_ShouldRefundCustomerAndRestoreStock() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order result = merchantService.rejectOrder(1L, "Out of stock");

        // Assert
        verify(customerRepository).save(argThat(c -> c.getBalance().compareTo(new BigDecimal("700.00")) == 0));
        verify(productRepository).save(argThat(p -> p.getStockCount() == 12));
        verify(orderRepository).save(argThat(o -> o.getStatus() == Order.OrderStatus.REJECT_BY_MERCHANT &&
                o.getRejectReason().equals("Out of stock")));
    }

    @Test
    void getProduct_WithValidId_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = merchantService.getProduct(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct.getId(), result.getId());
    }

    @Test
    void getProduct_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            merchantService.getProduct(999L);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void getMerchant_WithValidId_ShouldReturnMerchant() {
        // Arrange
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(testMerchant));

        // Act
        Merchant result = merchantService.getMerchant(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testMerchant.getId(), result.getId());
    }

    @Test
    void createBrand_ShouldSaveBrand() {
        // Arrange
        Brand newBrand = new Brand();
        newBrand.setName("Apple");
        when(brandRepository.save(any(Brand.class))).thenReturn(newBrand);

        // Act
        Brand result = merchantService.createBrand("Apple");

        // Assert
        assertNotNull(result);
        verify(brandRepository).save(argThat(b -> b.getName().equals("Apple")));
    }
}
