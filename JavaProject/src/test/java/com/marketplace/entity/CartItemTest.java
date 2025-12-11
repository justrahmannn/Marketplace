package com.marketplace.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// filepath: JavaProject/src/test/java/com/marketplace/entity/CartItemTest.java



class CartItemTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        CartItem cartItem = new CartItem();

        // Assert
        assertNotNull(cartItem);
        assertNull(cartItem.getId());
        assertNull(cartItem.getCart());
        assertNull(cartItem.getProduct());
        assertNull(cartItem.getCount());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        CartItem cartItem = new CartItem();
        Long id = 1L;

        // Act
        cartItem.setId(id);

        // Assert
        assertEquals(id, cartItem.getId());
    }

    @Test
    void testSetAndGetCart() {
        // Arrange
        CartItem cartItem = new CartItem();
        Cart cart = new Cart();
        cart.setId(1L);

        // Act
        cartItem.setCart(cart);

        // Assert
        assertEquals(cart, cartItem.getCart());
        assertEquals(1L, cartItem.getCart().getId());
    }

    @Test
    void testSetAndGetProduct() {
        // Arrange
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(1L);

        // Act
        cartItem.setProduct(product);

        // Assert
        assertEquals(product, cartItem.getProduct());
        assertEquals(1L, cartItem.getProduct().getId());
    }

    @Test
    void testSetAndGetCount() {
        // Arrange
        CartItem cartItem = new CartItem();
        Integer count = 5;

        // Act
        cartItem.setCount(count);

        // Assert
        assertEquals(count, cartItem.getCount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(1L);

        CartItem cartItem3 = new CartItem();
        cartItem3.setId(2L);

        // Act & Assert
        assertEquals(cartItem1, cartItem2);
        assertNotEquals(cartItem1, cartItem3);
        assertEquals(cartItem1.hashCode(), cartItem2.hashCode());
        assertNotEquals(cartItem1.hashCode(), cartItem3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCount(3);

        // Act
        String result = cartItem.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("count=3"));
    }
}