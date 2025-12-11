import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

package com.marketplace.entity;



class ProductPhotoTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        ProductPhoto productPhoto = new ProductPhoto();

        // Assert
        assertNotNull(productPhoto);
        assertNull(productPhoto.getId());
        assertNull(productPhoto.getProduct());
        assertNull(productPhoto.getUrl());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        ProductPhoto productPhoto = new ProductPhoto();
        Long id = 1L;

        // Act
        productPhoto.setId(id);

        // Assert
        assertEquals(id, productPhoto.getId());
    }

    @Test
    void testSetAndGetProduct() {
        // Arrange
        ProductPhoto productPhoto = new ProductPhoto();
        Product product = new Product();

        // Act
        productPhoto.setProduct(product);

        // Assert
        assertEquals(product, productPhoto.getProduct());
    }

    @Test
    void testSetAndGetUrl() {
        // Arrange
        ProductPhoto productPhoto = new ProductPhoto();
        String url = "http://example.com/photo.jpg";

        // Act
        productPhoto.setUrl(url);

        // Assert
        assertEquals(url, productPhoto.getUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ProductPhoto productPhoto1 = new ProductPhoto();
        productPhoto1.setId(1L);

        ProductPhoto productPhoto2 = new ProductPhoto();
        productPhoto2.setId(1L);

        ProductPhoto productPhoto3 = new ProductPhoto();
        productPhoto3.setId(2L);

        // Act & Assert
        assertEquals(productPhoto1, productPhoto2);
        assertNotEquals(productPhoto1, productPhoto3);
        assertEquals(productPhoto1.hashCode(), productPhoto2.hashCode());
        assertNotEquals(productPhoto1.hashCode(), productPhoto3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        ProductPhoto productPhoto = new ProductPhoto();
        productPhoto.setId(1L);
        productPhoto.setUrl("http://example.com/photo.jpg");

        // Act
        String result = productPhoto.toString();

        // Assert
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("url=http://example.com/photo.jpg"));
    }
}