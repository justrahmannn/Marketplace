package com.marketplace.entity;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// filepath: JavaProject/src/test/java/com/marketplace/entity/CategoryTest.java



class CategoryTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Category category = new Category();

        // Assert
        assertNotNull(category);
        assertNull(category.getId());
        assertNull(category.getName());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Category category = new Category();
        Long id = 1L;

        // Act
        category.setId(id);

        // Assert
        assertEquals(id, category.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        Category category = new Category();
        String name = "Electronics";

        // Act
        category.setName(name);

        // Assert
        assertEquals(name, category.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Electronics");

        Category category3 = new Category();
        category3.setId(2L);
        category3.setName("Clothing");

        // Act & Assert
        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1.hashCode(), category3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        // Act
        String result = category.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Electronics"));
    }
}