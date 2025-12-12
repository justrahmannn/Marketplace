package com.marketplace.service;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.entity.Review;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Review addReview(Long customerId, Long productId, Integer rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Check if customer exists
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if customer has purchased this product
        List<Order> customerOrders = orderRepository.findByCustomerId(customerId);
        boolean hasPurchased = customerOrders.stream()
                .anyMatch(order -> order.getProduct().getId().equals(productId)
                        && "DELIVERED".equals(order.getStatus()));

        if (!hasPurchased) {
            throw new RuntimeException("You can only review products you have purchased and received");
        }

        // Check if customer already reviewed this product
        if (reviewRepository.existsByProductIdAndCustomerId(productId, customerId)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        // Create review
        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);

        // Update product average rating
        updateProductRating(productId);

        return savedReview;
    }

    @Transactional
    public Review updateReview(Long reviewId, Integer rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(rating);
        review.setComment(comment);

        Review updatedReview = reviewRepository.save(review);

        // Update product average rating
        updateProductRating(review.getProduct().getId());

        return updatedReview;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Long productId = review.getProduct().getId();
        reviewRepository.delete(review);

        // Update product average rating
        updateProductRating(productId);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getCustomerReviews(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    public Review getReviewByProductAndCustomer(Long productId, Long customerId) {
        return reviewRepository.findByProductIdAndCustomerId(productId, customerId)
                .orElse(null);
    }

    @Transactional
    public void updateProductRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (reviews.isEmpty()) {
            product.setAverageRating(0.0);
            product.setReviewCount(0);
        } else {
            double average = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);

            product.setAverageRating(Math.round(average * 10.0) / 10.0); // Round to 1 decimal
            product.setReviewCount(reviews.size());
        }

        productRepository.save(product);
    }
}
