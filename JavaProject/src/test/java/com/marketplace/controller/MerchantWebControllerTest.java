import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.service.MerchantService;
import com.marketplace.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/controller/MerchantWebControllerTest.java
package com.marketplace.controller;




class MerchantWebControllerTest {

    @Mock
    private MerchantService merchantService;

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @InjectMocks
    private MerchantWebController merchantWebController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDashboard() {
        List<Product> products = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setStatus(Order.OrderStatus.CREATED);
        orders.add(order);

        when(merchantService.getProducts(1L)).thenReturn(products);
        when(merchantService.getOrders(1L)).thenReturn(orders);
        when(merchantService.getMerchant(1L)).thenReturn(new Object());

        String result = merchantWebController.dashboard(1L, model);

        assertEquals("merchant_dashboard", result);
        verify(model).addAttribute("merchant", new Object());
        verify(model).addAttribute("merchantId", 1L);
        verify(model).addAttribute("productCount", 0);
        verify(model).addAttribute("orderCount", 1);
        verify(model).addAttribute("pendingOrderCount", 1L);
    }

    @Test
    void testListProducts() {
        List<Product> products = new ArrayList<>();
        when(merchantService.getProducts(1L)).thenReturn(products);

        String result = merchantWebController.listProducts(1L, model);

        assertEquals("merchant_products", result);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("merchantId", 1L);
    }

    @Test
    void testAddProductForm() {
        when(customerService.getAllCategories()).thenReturn(new ArrayList<>());
        when(customerService.getAllBrands()).thenReturn(new ArrayList<>());

        String result = merchantWebController.addProductForm(1L, model);

        assertEquals("merchant_product_form", result);
        verify(model).addAttribute("product", new Product());
        verify(model).addAttribute("categories", new ArrayList<>());
        verify(model).addAttribute("brands", new ArrayList<>());
        verify(model).addAttribute("merchantId", 1L);
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        when(merchantService.createBrand("NewBrand")).thenReturn(new Object());

        String result = merchantWebController.addProduct(1L, product, "NewBrand");

        assertEquals("redirect:/merchant/products?merchantId=1", result);
        verify(merchantService).createBrand("NewBrand");
        verify(merchantService).addProduct(1L, product);
    }

    @Test
    void testEditProductForm() {
        Product product = new Product();
        product.setMerchant(new Object());
        when(merchantService.getProduct(1L)).thenReturn(product);
        when(customerService.getAllCategories()).thenReturn(new ArrayList<>());
        when(customerService.getAllBrands()).thenReturn(new ArrayList<>());

        String result = merchantWebController.editProductForm(1L, null, model);

        assertEquals("merchant_product_form", result);
        verify(model).addAttribute("product", product);
        verify(model).addAttribute("categories", new ArrayList<>());
        verify(model).addAttribute("brands", new ArrayList<>());
        verify(model).addAttribute("merchantId", product.getMerchant().getId());
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        when(merchantService.createBrand("UpdatedBrand")).thenReturn(new Object());

        String result = merchantWebController.editProduct(1L, 1L, product, "UpdatedBrand");

        assertEquals("redirect:/merchant/products?merchantId=1", result);
        verify(merchantService).createBrand("UpdatedBrand");
        verify(merchantService).updateProduct(1L, product);
    }

    @Test
    void testDeleteProduct() {
        String result = merchantWebController.deleteProduct(1L, 1L);

        assertEquals("redirect:/merchant/products?merchantId=1", result);
        verify(merchantService).deleteProduct(1L);
    }

    @Test
    void testListOrders() {
        List<Order> orders = new ArrayList<>();
        when(merchantService.getOrders(1L)).thenReturn(orders);

        String result = merchantWebController.listOrders(1L, model);

        assertEquals("merchant_orders", result);
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("merchantId", 1L);
    }

    @Test
    void testAcceptOrder() {
        String result = merchantWebController.acceptOrder(1L, 1L);

        assertEquals("redirect:/merchant/orders?merchantId=1", result);
        verify(merchantService).acceptOrder(1L);
    }

    @Test
    void testDeliverOrder() {
        String result = merchantWebController.deliverOrder(1L, 1L);

        assertEquals("redirect:/merchant/orders?merchantId=1", result);
        verify(merchantService).deliverOrder(1L);
    }

    @Test
    void testRejectOrder() {
        String result = merchantWebController.rejectOrder(1L, 1L, "Out of stock");

        assertEquals("redirect:/merchant/orders?merchantId=1", result);
        verify(merchantService).rejectOrder(1L, "Out of stock");
    }
}