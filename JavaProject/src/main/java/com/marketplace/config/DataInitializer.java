package com.marketplace.config;

import com.marketplace.entity.*;
import com.marketplace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("Məlumatlar yüklənir...");

        // Merchants
        Merchant merchantTech = createMerchant("TechStore", "Admin", "TechWorld Inc.");
        Merchant merchantFashion = createMerchant("FashionStore", "Admin", "Vogue Fashion");
        Merchant merchantSports = createMerchant("SportsStore", "Admin", "SportMax");

        // Categories
        Category catElectronics = createCategory("Elektronika");
        Category catFashion = createCategory("Geyim");
        Category catSports = createCategory("İdman");
        Category catHome = createCategory("Ev və Məişət");
        Category catBeauty = createCategory("Gözəllik");

        // Brands
        Brand apple = createBrand("Apple");
        Brand samsung = createBrand("Samsung");
        Brand sony = createBrand("Sony");
        Brand lg = createBrand("LG");
        Brand nike = createBrand("Nike");
        Brand adidas = createBrand("Adidas");
        Brand puma = createBrand("Puma");
        Brand zara = createBrand("Zara");
        Brand hm = createBrand("H&M");
        Brand loreal = createBrand("L'Oréal");

        // Electronics - Apple
        createProduct(merchantTech, catElectronics, apple, "iPhone 15 Pro Max", "256GB, Titanium Blue", "https://images.unsplash.com/photo-1695048133142-1a20484d2569", new BigDecimal("1899"), 45);
        createProduct(merchantTech, catElectronics, apple, "iPhone 15 Pro", "128GB, Natural Titanium", "https://images.unsplash.com/photo-1695048133142-1a20484d2569", new BigDecimal("1599"), 60);
        createProduct(merchantTech, catElectronics, apple, "iPhone 15", "128GB, Blue", "https://images.unsplash.com/photo-1695048133142-1a20484d2569", new BigDecimal("1299"), 80);
        createProduct(merchantTech, catElectronics, apple, "MacBook Pro 16\"", "M3 Max, 36GB RAM, 1TB SSD", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", new BigDecimal("3999"), 20);
        createProduct(merchantTech, catElectronics, apple, "MacBook Air 15\"", "M2, 16GB RAM, 512GB SSD", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", new BigDecimal("1899"), 35);
        createProduct(merchantTech, catElectronics, apple, "iPad Pro 12.9\"", "M2, 256GB, WiFi", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0", new BigDecimal("1599"), 40);
        createProduct(merchantTech, catElectronics, apple, "iPad Air", "M1, 64GB, WiFi", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0", new BigDecimal("899"), 50);
        createProduct(merchantTech, catElectronics, apple, "AirPods Pro 2", "USB-C, Active Noise Cancellation", "https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7", new BigDecimal("399"), 100);
        createProduct(merchantTech, catElectronics, apple, "AirPods Max", "Space Gray, Premium Sound", "https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7", new BigDecimal("849"), 25);
        createProduct(merchantTech, catElectronics, apple, "Apple Watch Series 9", "45mm, GPS + Cellular", "https://images.unsplash.com/photo-1434494878577-86c23bcb06b9", new BigDecimal("699"), 55);

        // Electronics - Samsung
        createProduct(merchantTech, catElectronics, samsung, "Galaxy S24 Ultra", "512GB, Titanium Gray", "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c", new BigDecimal("1799"), 50);
        createProduct(merchantTech, catElectronics, samsung, "Galaxy S24+", "256GB, Violet", "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c", new BigDecimal("1399"), 65);
        createProduct(merchantTech, catElectronics, samsung, "Galaxy Z Fold 5", "512GB, Phantom Black", "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c", new BigDecimal("2299"), 30);
        createProduct(merchantTech, catElectronics, samsung, "Galaxy Tab S9", "11\", 128GB, WiFi", "https://images.unsplash.com/photo-1561154464-82e9adf32764", new BigDecimal("899"), 40);
        createProduct(merchantTech, catElectronics, samsung, "Galaxy Buds2 Pro", "Graphite, ANC", "https://images.unsplash.com/photo-1590658268037-6bf12165a8df", new BigDecimal("349"), 75);
        createProduct(merchantTech, catElectronics, samsung, "Galaxy Watch 6", "44mm, Graphite", "https://images.unsplash.com/photo-1579586337278-3befd40fd17a", new BigDecimal("499"), 60);

        // Electronics - Sony
        createProduct(merchantTech, catElectronics, sony, "WH-1000XM5", "Noise Cancelling Headphones, Black", "https://images.unsplash.com/photo-1546435770-a3e426bf472b", new BigDecimal("599"), 80);
        createProduct(merchantTech, catElectronics, sony, "PlayStation 5", "Disc Edition, 1TB", "https://images.unsplash.com/photo-1606813907291-d86efa9b94db", new BigDecimal("799"), 40);
        createProduct(merchantTech, catElectronics, sony, "PlayStation 5 Digital", "Digital Edition, 1TB", "https://images.unsplash.com/photo-1606813907291-d86efa9b94db", new BigDecimal("699"), 35);
        createProduct(merchantTech, catElectronics, sony, "Sony Bravia XR 65\"", "4K OLED, Smart TV", "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1", new BigDecimal("2999"), 15);
        createProduct(merchantTech, catElectronics, sony, "Alpha A7 IV", "Full Frame Mirrorless Camera", "https://images.unsplash.com/photo-1502920917128-1aa500764cbd", new BigDecimal("3499"), 20);

        // Electronics - LG
        createProduct(merchantTech, catElectronics, lg, "LG OLED C3 55\"", "4K Smart TV, webOS", "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1", new BigDecimal("1899"), 25);
        createProduct(merchantTech, catElectronics, lg, "LG UltraGear 27\"", "Gaming Monitor, 240Hz", "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf", new BigDecimal("699"), 35);
        createProduct(merchantTech, catElectronics, lg, "LG Gram 17\"", "Laptop, Intel i7, 16GB RAM", "https://images.unsplash.com/photo-1496181133206-80ce9b88a853", new BigDecimal("1799"), 30);

        // Fashion - Nike
        createProduct(merchantFashion, catFashion, nike, "Air Jordan 1 High", "Retro, Chicago Colorway", "https://images.unsplash.com/photo-1542291026-7eec264c27ff", new BigDecimal("279"), 100);
        createProduct(merchantFashion, catFashion, nike, "Air Max 270", "Running Shoes, Black/White", "https://images.unsplash.com/photo-1542291026-7eec264c27ff", new BigDecimal("229"), 120);
        createProduct(merchantFashion, catFashion, nike, "Dunk Low", "Panda, Black/White", "https://images.unsplash.com/photo-1542291026-7eec264c27ff", new BigDecimal("189"), 150);
        createProduct(merchantFashion, catFashion, nike, "Tech Fleece Hoodie", "Premium Cotton, Gray", "https://images.unsplash.com/photo-1556821840-3a63f95609a7", new BigDecimal("149"), 80);
        createProduct(merchantFashion, catFashion, nike, "Sportswear Club T-Shirt", "Cotton, Multiple Colors", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab", new BigDecimal("49"), 200);
        createProduct(merchantFashion, catFashion, nike, "Pro Training Pants", "Dri-FIT, Black", "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80", new BigDecimal("89"), 150);

        // Fashion - Adidas
        createProduct(merchantFashion, catFashion, adidas, "Ultraboost 23", "Running Shoes, Core Black", "https://images.unsplash.com/photo-1608231387042-66d1773070a5", new BigDecimal("269"), 90);
        createProduct(merchantFashion, catFashion, adidas, "Stan Smith", "Classic Sneakers, White/Green", "https://images.unsplash.com/photo-1608231387042-66d1773070a5", new BigDecimal("149"), 130);
        createProduct(merchantFashion, catFashion, adidas, "Samba OG", "Retro Sneakers, Black/White", "https://images.unsplash.com/photo-1608231387042-66d1773070a5", new BigDecimal("159"), 110);
        createProduct(merchantFashion, catFashion, adidas, "Essentials Hoodie", "Cotton Blend, Navy", "https://images.unsplash.com/photo-1556821840-3a63f95609a7", new BigDecimal("99"), 100);
        createProduct(merchantFashion, catFashion, adidas, "Tiro Track Pants", "Training Pants, Black", "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80", new BigDecimal("79"), 140);

        // Fashion - Puma
        createProduct(merchantFashion, catFashion, puma, "RS-X", "Chunky Sneakers, Multi-Color", "https://images.unsplash.com/photo-1608231387042-66d1773070a5", new BigDecimal("179"), 70);
        createProduct(merchantFashion, catFashion, puma, "Suede Classic", "Retro Sneakers, Red", "https://images.unsplash.com/photo-1608231387042-66d1773070a5", new BigDecimal("119"), 90);
        createProduct(merchantFashion, catFashion, puma, "Essentials Logo Tee", "Cotton T-Shirt, White", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab", new BigDecimal("39"), 180);

        // Fashion - Zara
        createProduct(merchantFashion, catFashion, zara, "Tailored Blazer", "Wool Blend, Navy", "https://images.unsplash.com/photo-1507679799987-c73779587ccf", new BigDecimal("199"), 60);
        createProduct(merchantFashion, catFashion, zara, "Slim Fit Jeans", "Dark Blue Denim", "https://images.unsplash.com/photo-1542272604-787c3835535d", new BigDecimal("79"), 120);
        createProduct(merchantFashion, catFashion, zara, "Leather Jacket", "Genuine Leather, Black", "https://images.unsplash.com/photo-1551028719-00167b16eac5", new BigDecimal("299"), 40);
        createProduct(merchantFashion, catFashion, zara, "Oxford Shirt", "Cotton, White", "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf", new BigDecimal("69"), 100);

        // Fashion - H&M
        createProduct(merchantFashion, catFashion, hm, "Basic T-Shirt Pack", "3-Pack, Cotton, Mixed Colors", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab", new BigDecimal("39"), 200);
        createProduct(merchantFashion, catFashion, hm, "Skinny Jeans", "Stretch Denim, Black", "https://images.unsplash.com/photo-1542272604-787c3835535d", new BigDecimal("59"), 150);
        createProduct(merchantFashion, catFashion, hm, "Hooded Sweatshirt", "Cotton Blend, Gray", "https://images.unsplash.com/photo-1556821840-3a63f95609a7", new BigDecimal("49"), 180);
        createProduct(merchantFashion, catFashion, hm, "Bomber Jacket", "Nylon, Olive Green", "https://images.unsplash.com/photo-1551028719-00167b16eac5", new BigDecimal("89"), 80);

        // Sports
        createProduct(merchantSports, catSports, nike, "Football/Soccer Ball", "Official Size 5", "https://images.unsplash.com/photo-1614632537197-38a17061c2bd", new BigDecimal("49"), 100);
        createProduct(merchantSports, catSports, adidas, "Gym Bag", "Duffel Bag, Black", "https://images.unsplash.com/photo-1553062407-98eeb64c6a62", new BigDecimal("69"), 80);
        createProduct(merchantSports, catSports, puma, "Yoga Mat", "Non-Slip, Purple", "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f", new BigDecimal("39"), 120);
        createProduct(merchantSports, catSports, nike, "Resistance Bands Set", "5-Piece Set, Multiple Resistance", "https://images.unsplash.com/photo-1598289431512-b97b0917affc", new BigDecimal("29"), 150);

        // Home & Living
        createProduct(merchantTech, catHome, sony, "Soundbar HT-A7000", "7.1.2ch Dolby Atmos", "https://images.unsplash.com/photo-1545454675-3531b543be5d", new BigDecimal("1799"), 30);
        createProduct(merchantTech, catHome, lg, "Refrigerator InstaView", "French Door, 635L", "https://images.unsplash.com/photo-1571175443880-49e1d25b2bc5", new BigDecimal("3499"), 15);
        createProduct(merchantTech, catHome, samsung, "Washing Machine", "Front Load, 9kg, AI Control", "https://images.unsplash.com/photo-1626806787461-102c1bfaaea1", new BigDecimal("1299"), 20);
        createProduct(merchantTech, catHome, lg, "Air Purifier", "PuriCare 360°, HEPA Filter", "https://images.unsplash.com/photo-1585771724684-38269d6639fd", new BigDecimal("699"), 40);

        // Beauty
        createProduct(merchantFashion, catBeauty, loreal, "Revitalift Serum", "Anti-Aging, Hyaluronic Acid", "https://images.unsplash.com/photo-1556228578-0d85b1a4d571", new BigDecimal("49"), 100);
        createProduct(merchantFashion, catBeauty, loreal, "Elvive Shampoo", "Total Repair 5, 400ml", "https://images.unsplash.com/photo-1571781926291-c477ebfd024b", new BigDecimal("19"), 200);
        createProduct(merchantFashion, catBeauty, loreal, "Infallible Foundation", "24H Matte, Multiple Shades", "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9", new BigDecimal("29"), 150);

        // Demo Customer
        Customer customer = new Customer();
        customer.setName("ali");
        customer.setSurname("user");
        customer.setPassword("password");
        customer.setType(User.UserType.CUSTOMER);
        customer.setBalance(new BigDecimal("10000.00"));
        customer.setEmail("ali@marketplace.az");
        customer.setCardNumber("4169738577889900");
        customerRepository.save(customer);

        System.out.println("✓ Məlumatlar uğurla yükləndi!");
        System.out.println("✓ Demo İstifadəçi: ali / password");
        System.out.println("✓ " + productRepository.count() + " məhsul əlavə edildi");
    }

    Merchant createMerchant(String name, String surname, String companyName) {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setSurname(surname);
        merchant.setEmail(name.toLowerCase() + "@marketplace.az");
        merchant.setPassword("password");
        merchant.setType(User.UserType.MERCHANT);
        merchant.setCompanyName(companyName);
        return merchantRepository.save(merchant);
    }

    Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    Brand createBrand(String name) {
        Brand brand = new Brand();
        brand.setName(name);
        return brandRepository.save(brand);
    }

    void createProduct(Merchant merchant, Category category, Brand brand, String name, String details, String imageUrl, BigDecimal price, Integer stock) {
        Product product = new Product();
        product.setMerchant(merchant);
        product.setCategory(category);
        product.setBrand(brand);
        product.setName(name);
        product.setDetails(details);
        product.setPrice(price);
        product.setStockCount(stock);
        productRepository.save(product);
    }
}
