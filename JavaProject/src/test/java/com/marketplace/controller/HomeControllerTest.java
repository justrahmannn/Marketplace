import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

// filepath: JavaProject/src/test/java/com/marketplace/controller/HomeControllerTest.java
package com.marketplace.controller;



class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHome() {
        // Act
        String result = homeController.home();

        // Assert
        assertEquals("index", result, "The home method should return 'index'");
    }
}