package com.marketplace.service;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.entity.ProductPhoto;
import com.marketplace.entity.Brand;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.exception.InsufficientBalanceException;
import com.marketplace.repository.BrandRepository;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.MerchantRepository;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.ProductPhotoRepository;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;
    private final FileStorageService fileStorageService;
    private final ProductPhotoRepository productPhotoRepository;

    public Product createProduct(@org.springframework.lang.NonNull Long merchantId, Product product,
            MultipartFile[] images) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "id", merchantId));
        product.setMerchant(merchant);
        Product savedProduct = productRepository.save(product);

        // Save product images
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageUrl = fileStorageService.saveFile(image, "products");
                    ProductPhoto photo = new ProductPhoto();
                    photo.setProduct(savedProduct);
                    photo.setUrl(imageUrl);
                    productPhotoRepository.save(photo);
                }
            }
        }

        return savedProduct;
    }

    public Product updateProduct(@org.springframework.lang.NonNull Long productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        product.setName(productDetails.getName());
        product.setDetails(productDetails.getDetails());
        product.setPrice(productDetails.getPrice());
        product.setStockCount(productDetails.getStockCount());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());

        return productRepository.save(product);
    }

    public void deleteProduct(@org.springframework.lang.NonNull Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getMerchantProducts(@org.springframework.lang.NonNull Long merchantId) {
        return productRepository.findByMerchantId(merchantId);
    }

    public List<Order> getMerchantOrders(@org.springframework.lang.NonNull Long merchantId) {
        return orderRepository.findByProductMerchantId(merchantId);
    }

    public Order acceptOrder(@org.springframework.lang.NonNull Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setStatus(Order.OrderStatus.ACCEPTED);
        return orderRepository.save(order);
    }

    public Order deliverOrder(@org.springframework.lang.NonNull Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    public Order rejectOrder(@org.springframework.lang.NonNull Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Refund money to customer
        Customer customer = order.getCustomer();
        customer.setBalance(customer.getBalance().add(order.getTotalAmount()));
        customerRepository.save(customer);

        // Restore stock
        Product product = order.getProduct();
        product.setStockCount(product.getStockCount() + order.getCount());
        productRepository.save(product);

        // Update order
        order.setStatus(Order.OrderStatus.REJECT_BY_MERCHANT);
        order.setRejectReason(reason);
        return orderRepository.save(order);
    }

    // Helper methods for web controller
    public Product addProduct(@org.springframework.lang.NonNull Long merchantId, Product product,
            MultipartFile[] images) {
        return createProduct(merchantId, product, images);
    }

    public List<Product> getProducts(@org.springframework.lang.NonNull Long merchantId) {
        return getMerchantProducts(merchantId);
    }

    public List<Order> getOrders(@org.springframework.lang.NonNull Long merchantId) {
        return getMerchantOrders(merchantId);
    }

    public Product getProduct(@org.springframework.lang.NonNull Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        // Initialize photos to avoid LazyInitializationException
        if (product.getPhotos() != null) {
            product.getPhotos().size();
        }
        return product;
    }

    public Merchant getMerchant(@org.springframework.lang.NonNull Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", "id", merchantId));
    }

    public Brand createBrand(String name) {
        Brand brand = new Brand();
        brand.setName(name);
        return brandRepository.save(brand);
    }
}
