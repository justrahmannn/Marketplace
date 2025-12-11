import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

package com.marketplace.entity;




class WishlistTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Wishlist wishlist = new Wishlist();

        // Assert
        assertNotNull(wishlist);
        assertNull(wishlist.getId());
        assertNull(wishlist.getProduct());
        assertNull(wishlist.getCustomer());
        assertNull(wishlist.getCreatedAt());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        Long id = 1L;

        // Act
        wishlist.setId(id);

        // Assert
        assertEquals(id, wishlist.getId());
    }

    @Test
    void testSetAndGetProduct() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        // Act
        wishlist.setProduct(product);

        // Assert
        assertEquals(product, wishlist.getProduct());
        assertEquals(1L, wishlist.getProduct().getId());
        assertEquals("Test Product", wishlist.getProduct().getName());
    }

    @Test
    void testSetAndGetCustomer() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John");

        // Act
        wishlist.setCustomer(customer);

        // Assert
        assertEquals(customer, wishlist.getCustomer());
        assertEquals(1L, wishlist.getCustomer().getId());
        assertEquals("John", wishlist.getCustomer().getName());
    }

    @Test
    void testSetAndGetCreatedAt() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        wishlist.setCreatedAt(createdAt);

        // Assert
        assertEquals(createdAt, wishlist.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Wishlist wishlist1 = new Wishlist();
        wishlist1.setId(1L);

        Wishlist wishlist2 = new Wishlist();
        wishlist2.setId(1L);

        Wishlist wishlist3 = new Wishlist();
        wishlist3.setId(2L);

        // Act & Assert
        assertEquals(wishlist1, wishlist2);
        assertNotEquals(wishlist1, wishlist3);
        assertEquals(wishlist1.hashCode(), wishlist2.hashCode());
        assertNotEquals(wishlist1.hashCode(), wishlist3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));

        // Act
        String result = wishlist.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("createdAt=2023-01-01T12:00"));
    }
}