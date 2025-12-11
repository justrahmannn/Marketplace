package com.marketplace.entity;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// filepath: JavaProject/src/test/java/com/marketplace/entity/CartTest.java




class CartTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Cart cart = new Cart();

        // Assert
        assertNotNull(cart);
        assertNull(cart.getId());
        assertNull(cart.getCustomer());
        assertNull(cart.getItems());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Cart cart = new Cart();
        Long id = 1L;

        // Act
        cart.setId(id);

        // Assert
        assertEquals(id, cart.getId());
    }

    @Test
    void testSetAndGetCustomer() {
        // Arrange
        Cart cart = new Cart();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        // Act
        cart.setCustomer(customer);

        // Assert
        assertEquals(customer, cart.getCustomer());
        assertEquals(1L, cart.getCustomer().getId());
        assertEquals("John Doe", cart.getCustomer().getName());
    }

    @Test
    void testSetAndGetItems() {
        // Arrange
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        item1.setId(1L);
        CartItem item2 = new CartItem();
        item2.setId(2L);
        List<CartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        // Act
        cart.setItems(items);

        // Assert
        assertEquals(items, cart.getItems());
        assertEquals(2, cart.getItems().size());
        assertEquals(1L, cart.getItems().get(0).getId());
        assertEquals(2L, cart.getItems().get(1).getId());
    }

    @Test
    void testAddItemToCart() {
        // Arrange
        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setId(1L);
        List<CartItem> items = new ArrayList<>();
        cart.setItems(items);

        // Act
        cart.getItems().add(item);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals(item, cart.getItems().get(0));
    }

    @Test
    void testRemoveItemFromCart() {
        // Arrange
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        item1.setId(1L);
        CartItem item2 = new CartItem();
        item2.setId(2L);
        List<CartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        cart.setItems(items);

        // Act
        cart.getItems().remove(item1);

        // Assert
        assertEquals(1, cart.getItems().size());
        assertEquals(item2, cart.getItems().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Cart cart1 = new Cart();
        cart1.setId(1L);

        Cart cart2 = new Cart();
        cart2.setId(1L);

        Cart cart3 = new Cart();
        cart3.setId(2L);

        // Act & Assert
        assertEquals(cart1, cart2);
        assertNotEquals(cart1, cart3);
        assertEquals(cart1.hashCode(), cart2.hashCode());
        assertNotEquals(cart1.hashCode(), cart3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);

        // Act
        String result = cart.toString();

        // Assert
        assertTrue(result.contains("id=1"));
    }
}