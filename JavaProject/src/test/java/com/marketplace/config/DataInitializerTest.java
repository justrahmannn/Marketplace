import com.marketplace.entity.*;
import com.marketplace.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

package com.marketplace.config;




class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun_WhenDataAlreadyExists() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(1L);

        // Act
        dataInitializer.run();

        // Assert
        verify(userRepository, times(1)).count();
        verifyNoInteractions(merchantRepository, customerRepository, categoryRepository, productRepository, brandRepository);
    }

    @Test
    void testRun_WhenNoDataExists() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(0L);

        // Act
        dataInitializer.run();

        // Assert
        verify(userRepository, times(1)).count();
        verify(merchantRepository, atLeastOnce()).save(any(Merchant.class));
        verify(categoryRepository, atLeastOnce()).save(any(Category.class));
        verify(brandRepository, atLeastOnce()).save(any(Brand.class));
        verify(productRepository, atLeastOnce()).save(any(Product.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateMerchant() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setName("TechStore");
        merchant.setSurname("Admin");
        merchant.setEmail("techstore@marketplace.az");
        merchant.setPassword("password");
        merchant.setType(User.UserType.MERCHANT);
        merchant.setCompanyName("TechWorld Inc.");
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        // Act
        Merchant result = dataInitializer.createMerchant("TechStore", "Admin", "TechWorld Inc.");

        // Assert
        assertEquals("TechStore", result.getName());
        assertEquals("Admin", result.getSurname());
        assertEquals("techstore@marketplace.az", result.getEmail());
        assertEquals("password", result.getPassword());
        assertEquals(User.UserType.MERCHANT, result.getType());
        assertEquals("TechWorld Inc.", result.getCompanyName());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void testCreateCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Elektronika");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category result = dataInitializer.createCategory("Elektronika");

        // Assert
        assertEquals("Elektronika", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateBrand() {
        // Arrange
        Brand brand = new Brand();
        brand.setName("Apple");
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        // Act
        Brand result = dataInitializer.createBrand("Apple");

        // Assert
        assertEquals("Apple", result.getName());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void testCreateProduct() {
        // Arrange
        Merchant merchant = new Merchant();
        Category category = new Category();
        Brand brand = new Brand();
        Product product = new Product();
        product.setName("iPhone 15 Pro Max");
        product.setDetails("256GB, Titanium Blue");
        product.setPrice(new BigDecimal("1899"));
        product.setStockCount(45);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        dataInitializer.createProduct(merchant, category, brand, "iPhone 15 Pro Max", "256GB, Titanium Blue", "https://example.com/image.jpg", new BigDecimal("1899"), 45);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }
}