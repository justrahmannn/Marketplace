package com.marketplace.controller;

import com.marketplace.entity.Customer;
import com.marketplace.entity.Merchant;
import com.marketplace.entity.User;
import com.marketplace.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Landing page
    @GetMapping("")
    public String index() {
        return "redirect:/";
    }

    // CUSTOMER ROUTES
    @GetMapping("/customer/login")
    public String customerLoginPage() {
        return "customer_login";
    }

    @PostMapping("/customer/login")
    public String customerLogin(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = authService.loginByEmail(email, password);
            if (user.getType() != User.UserType.CUSTOMER) {
                model.addAttribute("error", "Bu hesab alıcı hesabı deyil");
                return "customer_login";
            }
            return "redirect:/customer/products?userId=" + user.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "customer_login";
        }
    }

    @GetMapping("/customer/register")
    public String customerRegisterPage() {
        return "customer_register";
    }

    @PostMapping("/customer/register")
    public String registerCustomer(@RequestParam String name,
                                   @RequestParam String surname,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setEmail(email);
        customer.setPassword(password);
        authService.registerCustomer(customer);
        return "redirect:/auth/customer/login";
    }

    // MERCHANT ROUTES
    @GetMapping("/merchant/login")
    public String merchantLoginPage() {
        return "merchant_login";
    }

    @PostMapping("/merchant/login")
    public String merchantLogin(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            User user = authService.loginByEmail(email, password);
            if (user.getType() != User.UserType.MERCHANT) {
                model.addAttribute("error", "Bu hesab satıcı hesabı deyil");
                return "merchant_login";
            }
            return "redirect:/merchant/dashboard?merchantId=" + user.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "merchant_login";
        }
    }

    @GetMapping("/merchant/register")
    public String merchantRegisterPage() {
        return "merchant_register";
    }

    @PostMapping("/merchant/register")
    public String registerMerchant(@RequestParam String name,
                                  @RequestParam String surname,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String companyName) {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setSurname(surname);
        merchant.setEmail(email);
        merchant.setPassword(password);
        merchant.setCompanyName(companyName);
        authService.registerMerchant(merchant);
        return "redirect:/auth/merchant/login";
    }
}
