package com.marketplace.service;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.MerchantRepository;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ProductRepository;
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

    public Product createProduct(Long merchantId, Product product) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
        product.setMerchant(merchant);
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productDetails.getName());
        product.setDetails(productDetails.getDetails());
        product.setPrice(productDetails.getPrice());
        product.setStockCount(productDetails.getStockCount());
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getMerchantProducts(Long merchantId) {
        return productRepository.findByMerchantId(merchantId);
    }

    public List<Order> getMerchantOrders(Long merchantId) {
        return orderRepository.findByProductMerchantId(merchantId);
    }

    public Order acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(Order.OrderStatus.ACCEPTED);
        return orderRepository.save(order);
    }

    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(Order.OrderStatus.DELIVERED);
        return orderRepository.save(order);
    }

    public Order rejectOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
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
    public Product addProduct(Long merchantId, Product product) {
        return createProduct(merchantId, product);
    }
    
    public List<Product> getProducts(Long merchantId) {
        return getMerchantProducts(merchantId);
    }
    
    public List<Order> getOrders(Long merchantId) {
        return getMerchantOrders(merchantId);
    }
    
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    public Merchant getMerchant(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
    }
}
