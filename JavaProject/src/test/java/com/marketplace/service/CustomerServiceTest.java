package com.marketplace.service;
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

// filepath: JavaProject/src/test/java/com/marketplace/service/CustomerServiceTest.java




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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCart_CartExists() {
        Cart cart = new Cart();
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));

        Cart result = customerService.getCart(1L);

        assertEquals(cart, result);
        verify(cartRepository).findByCustomerId(1L);
        verify(customerRepository, never()).findById(anyLong());
    }

    @Test
    void testGetCart_CartDoesNotExist() {
        Customer customer = new Customer();
        Cart cart = new Cart();
        cart.setCustomer(customer);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.empty());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = customerService.getCart(1L);

        assertEquals(cart, result);
        verify(cartRepository).findByCustomerId(1L);
        verify(customerRepository).findById(1L);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddToCart_NewItem() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        Product product = new Product();
        product.setId(1L);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        customerService.addToCart(1L, 1L, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(product, cart.getItems().get(0).getProduct());
        assertEquals(2, cart.getItems().get(0).getCount());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void testAddToCart_ExistingItem() {
        Cart cart = new Cart();
        Product product = new Product();
        product.setId(1L);
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setCount(2);
        cart.setItems(List.of(existingItem));

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        customerService.addToCart(1L, 1L, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, existingItem.getCount());
        verify(cartItemRepository).save(existingItem);
    }

    @Test
    void testRemoveFromCart() {
        customerService.removeFromCart(1L);

        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    void testMakeOrder_Success() {
        Cart cart = new Cart();
        Customer customer = new Customer();
        customer.setBalance(new BigDecimal("1000.00"));
        Product product = new Product();
        product.setStockCount(10);
        product.setPrice(new BigDecimal("100.00"));
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCount(2);
        cart.setItems(List.of(item));
        cart.setCustomer(customer);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));

        customerService.makeOrder(1L);

        assertEquals(new BigDecimal("800.00"), customer.getBalance());
        assertEquals(8, product.getStockCount());
        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(cart);
    }

    @Test
    void testMakeOrder_InsufficientBalance() {
        Cart cart = new Cart();
        Customer customer = new Customer();
        customer.setBalance(new BigDecimal("100.00"));
        Product product = new Product();
        product.setStockCount(10);
        product.setPrice(new BigDecimal("100.00"));
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setCount(2);
        cart.setItems(List.of(item));
        cart.setCustomer(customer);

        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.makeOrder(1L));
        assertEquals("Balans kifayət deyil. Lazım: 200.00 ₼, Mövcud: 100.00 ₼", exception.getMessage());
    }

    @Test
    void testAddToWishlist() {
        Customer customer = new Customer();
        Product product = new Product();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        customerService.addToWishlist(1L, 1L);

        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void testRemoveFromWishlist() {
        customerService.removeFromWishlist(1L);

        verify(wishlistRepository).deleteById(1L);
    }

    @Test
    void testGetWishlist() {
        List<Wishlist> wishlist = new ArrayList<>();
        when(wishlistRepository.findByCustomerId(1L)).thenReturn(wishlist);

        List<Wishlist> result = customerService.getWishlist(1L);

        assertEquals(wishlist, result);
        verify(wishlistRepository).findByCustomerId(1L);
    }

    @Test
    void testIncreaseBalance_Success() {
        Customer customer = new Customer();
        customer.setCardNumber("1234");
        customer.setBalance(new BigDecimal("100.00"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.increaseBalance(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), customer.getBalance());
        verify(customerRepository).save(customer);
    }

    @Test
    void testIncreaseBalance_NoCard() {
        Customer customer = new Customer();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.increaseBalance(1L, new BigDecimal("50.00")));
        assertEquals("Balans artırmaq üçün əvvəlcə bank kartı əlavə etməlisiniz", exception.getMessage());
    }
}