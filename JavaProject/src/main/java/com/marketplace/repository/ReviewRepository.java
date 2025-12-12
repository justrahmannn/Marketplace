package com.marketplace.repository;

import com.marketplace.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByCustomerId(Long customerId);

    Optional<Review> findByProductIdAndCustomerId(Long productId, Long customerId);

    boolean existsByProductIdAndCustomerId(Long productId, Long customerId);
}
