package com.marketplace.controller;

import com.marketplace.entity.Order;
import com.marketplace.entity.Product;
import com.marketplace.service.MerchantService;
import com.marketplace.service.CustomerService;
import com.marketplace.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantWebController {

    private final MerchantService merchantService;
    private final CustomerService customerService;
    private final FileStorageService fileStorageService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam long merchantId, Model model) {
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
    public String listProducts(@RequestParam long merchantId, Model model) {
        model.addAttribute("products", merchantService.getProducts(merchantId));
        model.addAttribute("merchantId", merchantId);
        return "merchant_products";
    }

    @GetMapping("/products/add")
    public String addProductForm(@RequestParam long merchantId, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", customerService.getAllCategories());
        model.addAttribute("brands", customerService.getAllBrands());
        model.addAttribute("merchantId", merchantId);
        return "merchant_product_form";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam long merchantId,
            @ModelAttribute Product product,
            @RequestParam(required = false) String newBrandName,
            @RequestParam(value = "productImages", required = false) MultipartFile[] productImages) {
        if (newBrandName != null && !newBrandName.trim().isEmpty()) {
            product.setBrand(merchantService.createBrand(newBrandName));
        }
        merchantService.addProduct(merchantId, product, productImages, fileStorageService);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @GetMapping("/products/edit/{productId}")
    public String editProductForm(@PathVariable long productId, @RequestParam(required = false) Long merchantId,
            Model model) {
        Product product = merchantService.getProduct(productId);
        if (merchantId == null) {
            if (product.getMerchant() != null) {
                merchantId = product.getMerchant().getId();
            } else {
                // Fallback or error handling if product has no merchant (should not happen)
                throw new RuntimeException("Product has no associated merchant");
            }
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", customerService.getAllCategories());
        model.addAttribute("brands", customerService.getAllBrands());
        model.addAttribute("merchantId", merchantId);
        return "merchant_product_form";
    }

    @PostMapping("/products/edit/{productId}")
    public String editProduct(@PathVariable long productId,
            @RequestParam long merchantId,
            @ModelAttribute Product product,
            @RequestParam(required = false) String newBrandName,
            @RequestParam(value = "productImages", required = false) MultipartFile[] productImages) {
        if (newBrandName != null && !newBrandName.trim().isEmpty()) {
            product.setBrand(merchantService.createBrand(newBrandName));
        }
        merchantService.updateProduct(productId, product, productImages, fileStorageService);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @PostMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable long productId, @RequestParam long merchantId) {
        merchantService.deleteProduct(productId);
        return "redirect:/merchant/products?merchantId=" + merchantId;
    }

    @GetMapping("/orders")
    public String listOrders(@RequestParam long merchantId, Model model) {
        model.addAttribute("orders", merchantService.getOrders(merchantId));
        model.addAttribute("merchantId", merchantId);
        return "merchant_orders";
    }

    @PostMapping("/orders/{orderId}/accept")
    public String acceptOrder(@PathVariable long orderId, @RequestParam long merchantId) {
        merchantService.acceptOrder(orderId);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }

    @PostMapping("/orders/{orderId}/deliver")
    public String deliverOrder(@PathVariable long orderId, @RequestParam long merchantId) {
        merchantService.deliverOrder(orderId);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }

    @PostMapping("/orders/{orderId}/reject")
    public String rejectOrder(@PathVariable long orderId, @RequestParam long merchantId, @RequestParam String reason) {
        merchantService.rejectOrder(orderId, reason);
        return "redirect:/merchant/orders?merchantId=" + merchantId;
    }
}
