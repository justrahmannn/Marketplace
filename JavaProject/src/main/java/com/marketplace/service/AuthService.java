package com.marketplace.service;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.User;
import com.marketplace.repository.CustomerRepository;
import com.marketplace.repository.MerchantRepository;
import com.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User loginByEmail(String email, String password) {
        // Try customer first
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
            return customer.get();
        }
        
        // Try merchant
        Optional<Merchant> merchant = merchantRepository.findByEmail(email);
        if (merchant.isPresent() && passwordEncoder.matches(password, merchant.get().getPassword())) {
            return merchant.get();
        }
        
        throw new RuntimeException("Yanlış email və ya şifrə");
    }

    public Merchant registerMerchant(Merchant merchant) {
        merchant.setType(User.UserType.MERCHANT);
        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        return merchantRepository.save(merchant);
    }

    public Customer registerCustomer(Customer customer) {
        customer.setType(User.UserType.CUSTOMER);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setBalance(new BigDecimal("1000.00"));
        return customerRepository.save(customer);
    }
}
