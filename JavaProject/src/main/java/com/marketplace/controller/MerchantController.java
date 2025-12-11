package com.marketplace.controller;

import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/{merchantId}/products")
    public ResponseEntity<Product> createProduct(@PathVariable long merchantId, @RequestBody Product product) {
        return ResponseEntity.ok(merchantService.createProduct(merchantId, product));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable long productId, @RequestBody Product product) {
        return ResponseEntity.ok(merchantService.updateProduct(productId, product));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId) {
        merchantService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{merchantId}/products")
    public ResponseEntity<List<Product>> getProducts(@PathVariable long merchantId) {
        return ResponseEntity.ok(merchantService.getMerchantProducts(merchantId));
    }

    @GetMapping("/{merchantId}/orders")
    public ResponseEntity<List<Order>> getOrders(@PathVariable long merchantId) {
        return ResponseEntity.ok(merchantService.getMerchantOrders(merchantId));
    }

    @PostMapping("/orders/{orderId}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(merchantService.acceptOrder(orderId));
    }

    @PostMapping("/orders/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(merchantService.deliverOrder(orderId));
    }

    @PostMapping("/orders/{orderId}/reject")
    public ResponseEntity<Order> rejectOrder(@PathVariable long orderId, @RequestBody(required = false) String reason) {
        return ResponseEntity.ok(merchantService.rejectOrder(orderId, reason != null ? reason : "Səbəb göstərilməyib"));
    }
}
