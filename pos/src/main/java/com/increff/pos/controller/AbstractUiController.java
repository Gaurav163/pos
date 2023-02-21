package com.increff.pos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
public class AbstractUiController {

    protected ModelAndView mav(String page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal().toString().equals("anonymousUser")) {
            ModelAndView mav = new ModelAndView("login.html");
            mav.addObject("authenticated", false);
            return mav;
        } else if (Objects.equals(page, "login.html") || Objects.equals(page, "signup.html")) {
            ModelAndView mav = new ModelAndView("index.html");
            mav.addObject("authenticated", true);
            mav.addObject("email", auth.getPrincipal().toString());
            mav.addObject("role", auth.getAuthorities().stream().findFirst().get().toString());
            return mav;
        } else {
            ModelAndView mav = new ModelAndView(page);
            mav.addObject("authenticated", true);
            mav.addObject("email", auth.getPrincipal().toString());
            mav.addObject("role", auth.getAuthorities().stream().findFirst().get().toString());
            return mav;
        }

    }

}
