package com.marketplace.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal totalAmount;
    private Integer count;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private String rejectReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum OrderStatus {
        CREATED, ACCEPTED, DELIVERED, REJECT_BY_CUSTOMER, REJECT_BY_MERCHANT
    }
}
