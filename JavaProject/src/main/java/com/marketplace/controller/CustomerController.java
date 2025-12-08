package com.marketplace.controller;

import com.marketplace.entity.Product;
import com.marketplace.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/products")
    public String listProducts(@RequestParam Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            Model model) {
        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = customerService.searchProducts(search);
        } else if (categoryId != null) {
            products = customerService.filterByCategory(categoryId);
        } else if (brandId != null) {
            products = customerService.filterByBrand(brandId);
        } else {
            products = customerService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", customerService.getAllCategories());
        model.addAttribute("brands", customerService.getAllBrands());
        model.addAttribute("userId", userId);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedBrandId", brandId);
        return "products";
    }

    @GetMapping("/products/{productId}")
    public String viewProduct(@PathVariable Long productId, @RequestParam Long userId, Model model) {
        model.addAttribute("product", customerService.getProduct(productId));
        model.addAttribute("userId", userId);
        return "product_detail";
    }

    @GetMapping("/account")
    public String viewAccount(@RequestParam Long userId, Model model) {
        model.addAttribute("customer", customerService.getCustomer(userId));
        model.addAttribute("orders", customerService.getCustomerOrders(userId));
        model.addAttribute("userId", userId);
        return "account";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer count) {
        customerService.addToCart(userId, productId, count);
        return "redirect:/customer/cart?userId=" + userId;
    }

    @GetMapping("/cart")
    public String viewCart(@RequestParam Long userId, Model model) {
        model.addAttribute("cart", customerService.getCart(userId));
        model.addAttribute("totalAmount", customerService.getCartTotal(userId));
        model.addAttribute("userId", userId);
        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long userId, @RequestParam Long cartItemId) {
        customerService.removeFromCart(cartItemId);
        return "redirect:/customer/cart?userId=" + userId;
    }

    @PostMapping("/order")
    public String makeOrder(@RequestParam Long userId, Model model) {
        try {
            customerService.makeOrder(userId);
            return "redirect:/customer/order-success?userId=" + userId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cart", customerService.getCart(userId));
            model.addAttribute("totalAmount", customerService.getCartTotal(userId));
            model.addAttribute("userId", userId);
            return "cart";
        }
    }

    @GetMapping("/order-success")
    public String orderSuccess(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("customer", customerService.getCustomer(userId));
        model.addAttribute("orders", customerService.getCustomerOrders(userId));
        return "order_success";
    }

    @PostMapping("/account/increase-balance")
    public String increaseBalance(@RequestParam Long userId, @RequestParam BigDecimal amount, Model model) {
        try {
            customerService.increaseBalance(userId, amount);
            return "redirect:/customer/account?userId=" + userId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customerService.getCustomer(userId));
            model.addAttribute("orders", customerService.getCustomerOrders(userId));
            model.addAttribute("userId", userId);
            return "account";
        }
    }

    @PostMapping("/account/add-card")
    public String addCard(@RequestParam Long userId,
            @RequestParam String cardNumber,
            @RequestParam String cardExpiryDate,
            @RequestParam String cardCvv) {
        customerService.addCard(userId, cardNumber, cardExpiryDate, cardCvv);
        return "redirect:/customer/account?userId=" + userId;
    }

    @PostMapping("/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        customerService.cancelOrder(orderId);
        return "redirect:/customer/account?userId=" + userId;
    }

    @GetMapping("/wishlist")
    public String viewWishlist(@RequestParam Long userId, Model model) {
        model.addAttribute("wishlist", customerService.getWishlist(userId));
        model.addAttribute("userId", userId);
        return "wishlist";
    }

    @PostMapping("/wishlist/add")
    public String addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        customerService.addToWishlist(userId, productId);
        return "redirect:/customer/wishlist?userId=" + userId;
    }

    @PostMapping("/wishlist/remove")
    public String removeFromWishlist(@RequestParam Long userId, @RequestParam Long wishlistId) {
        customerService.removeFromWishlist(wishlistId);
        return "redirect:/customer/wishlist?userId=" + userId;
    }
}
