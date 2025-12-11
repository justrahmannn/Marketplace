package com.marketplace.repository;

import com.marketplace.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByCustomerId(Long customerId);
    List<Wishlist> findByCreatedAtBefore(LocalDateTime dateTime);
}
