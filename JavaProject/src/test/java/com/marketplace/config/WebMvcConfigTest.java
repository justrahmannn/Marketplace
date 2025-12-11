package com.marketplace.config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;





class WebMvcConfigTest {

    @InjectMocks
    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddResourceHandlers() {
        // Arrange
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistry.ResourceHandlerRegistration registration = mock(ResourceHandlerRegistry.ResourceHandlerRegistration.class);
        when(registry.addResourceHandler("/uploads/**")).thenReturn(registration);
        when(registration.addResourceLocations(anyString())).thenReturn(registration);

        Path uploadDir = Paths.get("./uploads");
        String expectedPath = "file:/" + uploadDir.toFile().getAbsolutePath() + "/";

        // Act
        webMvcConfig.addResourceHandlers(registry);

        // Assert
        verify(registry).addResourceHandler("/uploads/**");
        verify(registration).addResourceLocations(expectedPath);
    }

    @Test
    void testLocaleResolver() {
        // Act
        LocaleResolver localeResolver = webMvcConfig.localeResolver();

        // Assert
        assertNotNull(localeResolver);
        assertTrue(localeResolver instanceof CookieLocaleResolver);

        CookieLocaleResolver clr = (CookieLocaleResolver) localeResolver;
        assertEquals("marketplace_lang", clr.getCookieName());
        assertEquals(java.util.Locale.forLanguageTag("az"), clr.getDefaultLocale());
        assertEquals(java.time.Duration.ofDays(30).getSeconds(), clr.getCookieMaxAge());
    }

    @Test
    void testLocaleChangeInterceptor() {
        // Act
        LocaleChangeInterceptor interceptor = webMvcConfig.localeChangeInterceptor();

        // Assert
        assertNotNull(interceptor);
        assertEquals("lang", interceptor.getParamName());
    }

    @Test
    void testAddInterceptors() {
        // Arrange
        org.springframework.web.servlet.config.annotation.InterceptorRegistry registry = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistry.class);

        LocaleChangeInterceptor interceptor = webMvcConfig.localeChangeInterceptor();

        // Act
        webMvcConfig.addInterceptors(registry);

        // Assert
        verify(registry).addInterceptor(interceptor);
    }
}