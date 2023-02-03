package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String homePage(Model model) {
        return "_index.html";
    }

    @GetMapping("/brand")
    public String brandPage(Model model) {
        return "_brand.html";
    }

    @GetMapping("/product")
    public String productPage(Model model) {
        return "_product.html";
    }

    @GetMapping("/order")
    public String orderPage(Model model) {
        return "_order.html";
    }

    @GetMapping("/inventory")
    public String inventoryPage(Model model) {
        return "_inventory.html";
    }

    @GetMapping("/report")
    public String reportPage(Model model) {
        return "report.html";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login.html";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        return "signup.html";
    }

}
