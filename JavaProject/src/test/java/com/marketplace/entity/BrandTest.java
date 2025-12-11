import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// filepath: JavaProject/src/test/java/com/marketplace/entity/BrandTest.java
package com.marketplace.entity;



class BrandTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Brand brand = new Brand();

        // Assert
        assertNotNull(brand);
        assertNull(brand.getId());
        assertNull(brand.getName());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Brand brand = new Brand();
        Long id = 1L;

        // Act
        brand.setId(id);

        // Assert
        assertEquals(id, brand.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        Brand brand = new Brand();
        String name = "Test Brand";

        // Act
        brand.setName(name);

        // Assert
        assertEquals(name, brand.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setName("Brand A");

        Brand brand2 = new Brand();
        brand2.setId(1L);
        brand2.setName("Brand A");

        Brand brand3 = new Brand();
        brand3.setId(2L);
        brand3.setName("Brand B");

        // Act & Assert
        assertEquals(brand1, brand2);
        assertNotEquals(brand1, brand3);
        assertEquals(brand1.hashCode(), brand2.hashCode());
        assertNotEquals(brand1.hashCode(), brand3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Brand A");

        // Act
        String result = brand.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Brand A"));
    }
}