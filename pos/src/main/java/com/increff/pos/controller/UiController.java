package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UiController extends AbstractUiController {

    @GetMapping("/")
    public ModelAndView homePage(Model model) {
        return mav("index.html");
    }

    @GetMapping("/brand")
    public ModelAndView brandPage(Model model) {
        return mav("brand.html");
    }

    @GetMapping("/product")
    public ModelAndView productPage(Model model) {
        return mav("product.html");
    }

    @GetMapping("/order")
    public ModelAndView orderPage(Model model) {
        return mav("order.html");
    }

    @GetMapping("/inventory")
    public ModelAndView inventoryPage(Model model) {
        return mav("inventory.html");
    }

    @GetMapping("/report/{name}")
    public ModelAndView reportPage(Model model, @PathVariable("name") String name) {
        return mav(name + "report.html");
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "login.html";
    }

    @GetMapping("/user/signup")
    public String signupPage(Model model) {
        return "signup.html";
    }

}
