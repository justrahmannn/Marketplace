package com.marketplace.entity;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;





class ProductTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Product product = new Product();

        // Assert
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getCategory());
        assertNull(product.getMerchant());
        assertNull(product.getBrand());
        assertNull(product.getName());
        assertNull(product.getDetails());
        assertNull(product.getPrice());
        assertNull(product.getStockCount());
        assertNull(product.getPhotos());
        assertNull(product.getCreatedAt());
        assertNull(product.getUpdatedAt());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Product product = new Product();
        Long id = 1L;

        // Act
        product.setId(id);

        // Assert
        assertEquals(id, product.getId());
    }

    @Test
    void testSetAndGetCategory() {
        // Arrange
        Product product = new Product();
        Category category = new Category();

        // Act
        product.setCategory(category);

        // Assert
        assertEquals(category, product.getCategory());
    }

    @Test
    void testSetAndGetMerchant() {
        // Arrange
        Product product = new Product();
        Merchant merchant = new Merchant();

        // Act
        product.setMerchant(merchant);

        // Assert
        assertEquals(merchant, product.getMerchant());
    }

    @Test
    void testSetAndGetBrand() {
        // Arrange
        Product product = new Product();
        Brand brand = new Brand();

        // Act
        product.setBrand(brand);

        // Assert
        assertEquals(brand, product.getBrand());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        Product product = new Product();
        String name = "Test Product";

        // Act
        product.setName(name);

        // Assert
        assertEquals(name, product.getName());
    }

    @Test
    void testSetAndGetDetails() {
        // Arrange
        Product product = new Product();
        String details = "This is a test product.";

        // Act
        product.setDetails(details);

        // Assert
        assertEquals(details, product.getDetails());
    }

    @Test
    void testSetAndGetPrice() {
        // Arrange
        Product product = new Product();
        BigDecimal price = new BigDecimal("99.99");

        // Act
        product.setPrice(price);

        // Assert
        assertEquals(price, product.getPrice());
    }

    @Test
    void testSetAndGetStockCount() {
        // Arrange
        Product product = new Product();
        Integer stockCount = 50;

        // Act
        product.setStockCount(stockCount);

        // Assert
        assertEquals(stockCount, product.getStockCount());
    }

    @Test
    void testSetAndGetPhotos() {
        // Arrange
        Product product = new Product();
        List<ProductPhoto> photos = new ArrayList<>();

        // Act
        product.setPhotos(photos);

        // Assert
        assertEquals(photos, product.getPhotos());
    }

    @Test
    void testSetAndGetCreatedAt() {
        // Arrange
        Product product = new Product();
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        product.setCreatedAt(createdAt);

        // Assert
        assertEquals(createdAt, product.getCreatedAt());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        // Arrange
        Product product = new Product();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        product.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(updatedAt, product.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);

        Product product2 = new Product();
        product2.setId(1L);

        Product product3 = new Product();
        product3.setId(2L);

        // Act & Assert
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("99.99"));

        // Act
        String result = product.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Test Product"));
        assertTrue(result.contains("price=99.99"));
    }
}