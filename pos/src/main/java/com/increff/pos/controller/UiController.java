package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UiController extends AbstractUiController {

    @GetMapping("/")
    public ModelAndView homePage(Model model) {
        return mav("_index.html");
    }

    @GetMapping("/brand")
    public ModelAndView brandPage(Model model) {
        return mav("_brand.html");
    }

    @GetMapping("/product")
    public ModelAndView productPage(Model model) {
        return mav("_product.html");
    }

    @GetMapping("/order")
    public ModelAndView orderPage(Model model) {
        return mav("_order.html");
    }

    @GetMapping("/inventory")
    public ModelAndView inventoryPage(Model model) {
        return mav("_inventory.html");
    }

    @GetMapping("/report")
    public ModelAndView reportPage(Model model) {
        return mav("report.html");
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "login.html";
    }

    @GetMapping("/user/signup")
    public String signupPage(Model model) {
        return "signup.html";
    }

    @RequestMapping(path = "/user/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("pathcalled");
        Cookie newCookie = new Cookie("access_token", null);
        newCookie.setPath("/");
        newCookie.setHttpOnly(true);
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);
        return "login.html";
    }

}
