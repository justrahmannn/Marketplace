package com.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchants")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Merchant extends User {

    @Column(unique = true, nullable = false)
    private String email;
    
    private String companyName;
}
