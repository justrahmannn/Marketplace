import com.marketplace.entity.*;
import com.marketplace.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/service/MerchantServiceTest.java
package com.marketplace.service;




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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        Merchant merchant = new Merchant();
        Product product = new Product();
        Product savedProduct = new Product();

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = merchantService.createProduct(1L, product);

        assertEquals(savedProduct, result);
        assertEquals(merchant, product.getMerchant());
        verify(merchantRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void testCreateProduct_MerchantNotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            merchantService.createProduct(1L, new Product())
        );

        assertEquals("Merchant not found", exception.getMessage());
        verify(merchantRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateProduct_Success() {
        Product existingProduct = new Product();
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Name");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = merchantService.updateProduct(1L, updatedDetails);

        assertEquals(existingProduct, result);
        assertEquals("Updated Name", existingProduct.getName());
        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            merchantService.updateProduct(1L, new Product())
        );

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testDeleteProduct() {
        merchantService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void testGetMerchantProducts() {
        List<Product> products = new ArrayList<>();
        when(productRepository.findByMerchantId(1L)).thenReturn(products);

        List<Product> result = merchantService.getMerchantProducts(1L);

        assertEquals(products, result);
        verify(productRepository).findByMerchantId(1L);
    }

    @Test
    void testGetMerchantOrders() {
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findByProductMerchantId(1L)).thenReturn(orders);

        List<Order> result = merchantService.getMerchantOrders(1L);

        assertEquals(orders, result);
        verify(orderRepository).findByProductMerchantId(1L);
    }

    @Test
    void testAcceptOrder_Success() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = merchantService.acceptOrder(1L);

        assertEquals(Order.OrderStatus.ACCEPTED, order.getStatus());
        assertEquals(order, result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void testAcceptOrder_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            merchantService.acceptOrder(1L)
        );

        assertEquals("Order not found", exception.getMessage());
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testRejectOrder_Success() {
        Order order = new Order();
        Customer customer = new Customer();
        Product product = new Product();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setCount(2);
        customer.setBalance(new BigDecimal("500.00"));
        product.setStockCount(10);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order result = merchantService.rejectOrder(1L, "Out of stock");

        assertEquals(Order.OrderStatus.REJECT_BY_MERCHANT, order.getStatus());
        assertEquals("Out of stock", order.getRejectReason());
        assertEquals(new BigDecimal("600.00"), customer.getBalance());
        assertEquals(12, product.getStockCount());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
        verify(customerRepository).save(customer);
        verify(productRepository).save(product);
    }

    @Test
    void testCreateBrand() {
        Brand brand = new Brand();
        brand.setName("New Brand");
        when(brandRepository.save(brand)).thenReturn(brand);

        Brand result = merchantService.createBrand("New Brand");

        assertEquals(brand, result);
        verify(brandRepository).save(brand);
    }
}