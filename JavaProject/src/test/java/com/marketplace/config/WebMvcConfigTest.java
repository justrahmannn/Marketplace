package com.marketplace.config;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

class WebMvcConfigTest {

    @Test
    void localeResolver_defaults() {
        WebMvcConfig cfg = new WebMvcConfig();
        LocaleResolver resolver = cfg.localeResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof CookieLocaleResolver);

        // Verify the resolver returns the configured default locale for a bare request
        MockHttpServletRequest req = new MockHttpServletRequest();
        Locale resolved = resolver.resolveLocale(req);
        assertEquals(Locale.forLanguageTag("az"), resolved);
    }

    @Test
    void localeChangeInterceptor_paramName() {
        WebMvcConfig cfg = new WebMvcConfig();
        var interceptor = cfg.localeChangeInterceptor();
        assertNotNull(interceptor);
        assertTrue(interceptor instanceof LocaleChangeInterceptor);
        LocaleChangeInterceptor lci = interceptor;
        assertEquals("lang", lci.getParamName());
    }
}
