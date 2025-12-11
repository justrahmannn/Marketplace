import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/controller/MerchantControllerTest.java
package com.marketplace.controller;




class MerchantControllerTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController merchantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        when(merchantService.createProduct(1L, product)).thenReturn(product);

        ResponseEntity<Product> response = merchantController.createProduct(1L, product);

        assertEquals(ResponseEntity.ok(product), response);
        verify(merchantService).createProduct(1L, product);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        when(merchantService.updateProduct(1L, product)).thenReturn(product);

        ResponseEntity<Product> response = merchantController.updateProduct(1L, product);

        assertEquals(ResponseEntity.ok(product), response);
        verify(merchantService).updateProduct(1L, product);
    }

    @Test
    void testDeleteProduct() {
        ResponseEntity<Void> response = merchantController.deleteProduct(1L);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(merchantService).deleteProduct(1L);
    }

    @Test
    void testGetProducts() {
        List<Product> products = new ArrayList<>();
        when(merchantService.getMerchantProducts(1L)).thenReturn(products);

        ResponseEntity<List<Product>> response = merchantController.getProducts(1L);

        assertEquals(ResponseEntity.ok(products), response);
        verify(merchantService).getMerchantProducts(1L);
    }

    @Test
    void testGetOrders() {
        List<Order> orders = new ArrayList<>();
        when(merchantService.getMerchantOrders(1L)).thenReturn(orders);

        ResponseEntity<List<Order>> response = merchantController.getOrders(1L);

        assertEquals(ResponseEntity.ok(orders), response);
        verify(merchantService).getMerchantOrders(1L);
    }

    @Test
    void testAcceptOrder() {
        Order order = new Order();
        when(merchantService.acceptOrder(1L)).thenReturn(order);

        ResponseEntity<Order> response = merchantController.acceptOrder(1L);

        assertEquals(ResponseEntity.ok(order), response);
        verify(merchantService).acceptOrder(1L);
    }

    @Test
    void testDeliverOrder() {
        Order order = new Order();
        when(merchantService.deliverOrder(1L)).thenReturn(order);

        ResponseEntity<Order> response = merchantController.deliverOrder(1L);

        assertEquals(ResponseEntity.ok(order), response);
        verify(merchantService).deliverOrder(1L);
    }

    @Test
    void testRejectOrder_WithReason() {
        Order order = new Order();
        when(merchantService.rejectOrder(1L, "Out of stock")).thenReturn(order);

        ResponseEntity<Order> response = merchantController.rejectOrder(1L, "Out of stock");

        assertEquals(ResponseEntity.ok(order), response);
        verify(merchantService).rejectOrder(1L, "Out of stock");
    }

    @Test
    void testRejectOrder_WithoutReason() {
        Order order = new Order();
        when(merchantService.rejectOrder(1L, "Səbəb göstərilməyib")).thenReturn(order);

        ResponseEntity<Order> response = merchantController.rejectOrder(1L, null);

        assertEquals(ResponseEntity.ok(order), response);
        verify(merchantService).rejectOrder(1L, "Səbəb göstərilməyib");
    }
}