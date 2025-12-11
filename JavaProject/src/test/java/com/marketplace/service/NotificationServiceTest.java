import com.marketplace.entity.Customer;
import com.marketplace.entity.Product;
import com.marketplace.entity.Wishlist;
import com.marketplace.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

// filepath: JavaProject/src/test/java/com/marketplace/service/NotificationServiceTest.java
package com.marketplace.service;




class NotificationServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendWishlistNotifications_WithOldItems() {
        // Arrange
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Wishlist> oldWishlistItems = new ArrayList<>();

        Customer customer = new Customer();
        customer.setName("John Doe");

        Product product = new Product();
        product.setName("Sample Product");

        Wishlist wishlistItem = new Wishlist();
        wishlistItem.setCustomer(customer);
        wishlistItem.setProduct(product);
        wishlistItem.setCreatedAt(threeDaysAgo.minusDays(1));

        oldWishlistItems.add(wishlistItem);

        when(wishlistRepository.findByCreatedAtBefore(threeDaysAgo)).thenReturn(oldWishlistItems);

        // Act
        notificationService.sendWishlistNotifications();

        // Assert
        verify(wishlistRepository).findByCreatedAtBefore(threeDaysAgo);
        // Verify that the notification logic is executed (in this case, a print statement)
        // You can use a logger or other mechanisms in real-world scenarios
    }

    @Test
    void testSendWishlistNotifications_NoOldItems() {
        // Arrange
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Wishlist> oldWishlistItems = new ArrayList<>();

        when(wishlistRepository.findByCreatedAtBefore(threeDaysAgo)).thenReturn(oldWishlistItems);

        // Act
        notificationService.sendWishlistNotifications();

        // Assert
        verify(wishlistRepository).findByCreatedAtBefore(threeDaysAgo);
        // No notifications should be sent since the list is empty
        verifyNoMoreInteractions(wishlistRepository);
    }
}