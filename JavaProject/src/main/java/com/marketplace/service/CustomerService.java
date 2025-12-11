package com.marketplace.service;

import com.marketplace.entity.*;
import com.marketplace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final WishlistRepository wishlistRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public Cart getCart(@org.springframework.lang.NonNull Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> new RuntimeException("Customer not found"));
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    return cartRepository.save(cart);
                });
    }

    public void addToCart(@org.springframework.lang.NonNull Long customerId,
            @org.springframework.lang.NonNull Long productId, Integer count) {
        Cart cart = getCart(customerId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setCount(item.getCount() + count);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setCount(count);
            // Initialize list if null (though usually handled by JPA/Lombok if initialized)
            if (cart.getItems() == null)
                cart.setItems(new ArrayList<>());
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
    }

    public void removeFromCart(@org.springframework.lang.NonNull Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void makeOrder(@org.springframework.lang.NonNull Long customerId) {
        Cart cart = getCart(customerId);
        List<CartItem> items = cart.getItems();

        if (items.isEmpty()) {
            throw new RuntimeException("Səbət boşdur");
        }

        Customer customer = cart.getCustomer();
        BigDecimal totalCost = BigDecimal.ZERO;

        // Check stock availability first
        for (CartItem item : items) {
            if (item.getProduct().getStockCount() < item.getCount()) {
                throw new RuntimeException(item.getProduct().getName()
                        + " məhsulundan kifayət qədər stok yoxdur. Mövcud: " + item.getProduct().getStockCount());
            }
            BigDecimal itemTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getCount()));
            totalCost = totalCost.add(itemTotal);
        }

        // Check balance
        if (customer.getBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException(
                    "Balans kifayət deyil. Lazım: " + totalCost + " ₼, Mövcud: " + customer.getBalance() + " ₼");
        }

        // Create orders and reduce stock
        for (CartItem item : items) {
            Product product = item.getProduct();

            Order order = new Order();
            order.setCustomer(customer);
            order.setProduct(product);
            order.setCount(item.getCount());
            order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(item.getCount())));
            order.setStatus(Order.OrderStatus.CREATED);
            orderRepository.save(order);

            // Reduce stock
            product.setStockCount(product.getStockCount() - item.getCount());
            productRepository.save(product);
        }

        // Deduct balance
        customer.setBalance(customer.getBalance().subtract(totalCost));
        customerRepository.save(customer);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public void addToWishlist(@org.springframework.lang.NonNull Long customerId,
            @org.springframework.lang.NonNull Long productId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(@org.springframework.lang.NonNull Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public List<Wishlist> getWishlist(@org.springframework.lang.NonNull Long customerId) {
        return wishlistRepository.findByCustomerId(customerId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStockCount() > 0)
                .toList();
    }

    public Product getProduct(@org.springframework.lang.NonNull Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(query).stream()
                .filter(p -> p.getStockCount() > 0)
                .toList();
    }

    public List<Product> filterByCategory(@org.springframework.lang.NonNull Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .filter(p -> p.getStockCount() > 0)
                .toList();
    }

    public List<Product> filterByBrand(@org.springframework.lang.NonNull Long brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .filter(p -> p.getStockCount() > 0)
                .toList();
    }

    public List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .filter(p -> p.getStockCount() > 0)
                .toList();
    }

    public Customer getCustomer(@org.springframework.lang.NonNull Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void increaseBalance(@org.springframework.lang.NonNull Long customerId, BigDecimal amount) {
        Customer customer = getCustomer(customerId);
        if (customer.getCardNumber() == null || customer.getCardNumber().trim().isEmpty()) {
            throw new RuntimeException("Balans artırmaq üçün əvvəlcə bank kartı əlavə etməlisiniz");
        }
        customer.setBalance(customer.getBalance().add(amount));
        customerRepository.save(customer);
    }

    public void addCard(@org.springframework.lang.NonNull Long customerId, String cardNumber, String cardExpiryDate,
            String cardCvv) {
        Customer customer = getCustomer(customerId);
        customer.setCardNumber(cardNumber);
        customer.setCardExpiryDate(cardExpiryDate);
        customer.setCardCvv(cardCvv);
        customerRepository.save(customer);
    }

    public BigDecimal getCartTotal(@org.springframework.lang.NonNull Long customerId) {
        Cart cart = getCart(customerId);
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Order> getCustomerOrders(@org.springframework.lang.NonNull Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public void cancelOrder(@org.springframework.lang.NonNull Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.CREATED) {
            throw new RuntimeException("Yalnız gözləyən sifarişlər ləğv edilə bilər");
        }

        // Refund money
        Customer customer = order.getCustomer();
        customer.setBalance(customer.getBalance().add(order.getTotalAmount()));
        customerRepository.save(customer);

        // Restore stock
        Product product = order.getProduct();
        product.setStockCount(product.getStockCount() + order.getCount());
        productRepository.save(product);

        // Update order status
        order.setStatus(Order.OrderStatus.REJECT_BY_CUSTOMER);
        orderRepository.save(order);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
