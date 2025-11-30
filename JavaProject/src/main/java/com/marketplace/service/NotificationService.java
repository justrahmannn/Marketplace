package com.marketplace.service;

import com.marketplace.entity.Wishlist;
import com.marketplace.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final WishlistRepository wishlistRepository;

    // Run every day
    @Scheduled(fixedRate = 86400000)
    public void sendWishlistNotifications() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Wishlist> oldWishlistItems = wishlistRepository.findByCreatedAtBefore(threeDaysAgo);

        for (Wishlist item : oldWishlistItems) {
            // Logic to send notification
            System.out.println("Sending notification to user " + item.getCustomer().getName() + 
                               " about product " + item.getProduct().getName() + 
                               " in wishlist for more than 3 days.");
        }
    }
}
