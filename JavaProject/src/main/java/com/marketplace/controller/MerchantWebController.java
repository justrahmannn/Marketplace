package com.marketplace.controller;

import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.service.MerchantService;
import com.marketplace.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantWebController {

    private final MerchantService merchantService;
    private final CustomerService customerService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam Long merchantId, Model model) {
        List<Product> products = merchantService.getProducts(merchantId);
        List<Order> orders = merchantService.getOrders(merchantId);
        long pendingOrders = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.CREATED)
                .count();
        
        model.addAttribute("merchant", merchantService.getMerchant(merchantId));
        model.addAttribute("merchantId", merchantId);
        model.addAttribute("productCount", products.size());
        model.addAttribute("orderCount", orders.size());
        model.addAttribute("pendingOrderCount", pendingOrders);
        return "merchant_dashboard";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam Long merchantId, Model model) {
        model.addAttribute("products", merchantService.getProducts(merchantId));
        model.addAttribute("merchantId", merchantId);
        return "merchant_products";
    }

    @GetMapping("/products/add")
    public String addProductForm(@RequestParam Long merchantId, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", customerService.getAllCategories());
        model.addAttribute("brands", customerService.getAllBrands());
        model.addAttribute("merchantId", merchantId);
        return "merchant_product_form";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam Long merchantId, @ModelAttribute Product product) {
        merchantService.addProduct(merchantId, product);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @GetMapping("/products/edit/{productId}")
    public String editProductForm(@PathVariable Long productId, @RequestParam Long merchantId, Model model) {
        model.addAttribute("product", merchantService.getProduct(productId));
        model.addAttribute("categories", customerService.getAllCategories());
        model.addAttribute("brands", customerService.getAllBrands());
        model.addAttribute("merchantId", merchantId);
        return "merchant_product_form";
    }

    @PostMapping("/products/edit/{productId}")
    public String editProduct(@PathVariable Long productId, @RequestParam Long merchantId, @ModelAttribute Product product) {
        merchantService.updateProduct(productId, product);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @PostMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, @RequestParam Long merchantId) {
        merchantService.deleteProduct(productId);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @GetMapping("/orders")
    public String listOrders(@RequestParam Long merchantId, Model model) {
        model.addAttribute("orders", merchantService.getOrders(merchantId));
        model.addAttribute("merchantId", merchantId);
        return "merchant_orders";
    }

    @PostMapping("/orders/{orderId}/accept")
    public String acceptOrder(@PathVariable Long orderId, @RequestParam Long merchantId) {
        merchantService.acceptOrder(orderId);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }

    @PostMapping("/orders/{orderId}/deliver")
    public String deliverOrder(@PathVariable Long orderId, @RequestParam Long merchantId) {
        merchantService.deliverOrder(orderId);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }

    @PostMapping("/orders/{orderId}/reject")
    public String rejectOrder(@PathVariable Long orderId, @RequestParam Long merchantId, @RequestParam String reason) {
        merchantService.rejectOrder(orderId, reason);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }
}
