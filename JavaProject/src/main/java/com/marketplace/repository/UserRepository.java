package com.marketplace.repository;

import com.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name); // Assuming name is username for login, or we might need a separate username field.
    // The prompt says "Name", "Surname", "Password". Usually login is via email or unique username.
    // I'll assume "Name" is unique for now or just add a method to find by name.
    // Actually, for login, usually it's email or username. The prompt just lists "Name".
    // I will stick to finding by Name for now.
}
