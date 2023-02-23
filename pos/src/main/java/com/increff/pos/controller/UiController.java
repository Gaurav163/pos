package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UiController extends AbstractUiController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView homePage(Model model) {
        return mav("index.html");
    }

    @RequestMapping(path = "/brand", method = RequestMethod.GET)
    public ModelAndView brandPage(Model model) {
        return mav("brand.html");
    }

    @RequestMapping(path = "/product", method = RequestMethod.GET)
    public ModelAndView productPage(Model model) {
        return mav("product.html");
    }

    @RequestMapping(path = "/order", method = RequestMethod.GET)
    public ModelAndView orderPage(Model model) {
        return mav("order.html");
    }

    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public ModelAndView inventoryPage(Model model) {
        return mav("inventory.html");
    }

    @RequestMapping(path = "/reports/{name}", method = RequestMethod.GET)
    public ModelAndView reportPage(Model model, @PathVariable("name") String name) {
        return mav(name + "report.html");
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.GET)
    public ModelAndView loginPage(Model model) {
        return mav("login.html");
    }

    @RequestMapping(path = "/user/signup", method = RequestMethod.GET)
    public ModelAndView signupPage(Model model) {
        return mav("signup.html");
    }

}
