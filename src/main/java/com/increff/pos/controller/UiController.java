package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String homePage(Model model) {
        return "index.html";
    }

    @GetMapping("/brand")
    public String brandPage(Model model) {
        return "brand.html";
    }
}
