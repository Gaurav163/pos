package com.increff.pos.controller;


import com.increff.pos.dto.UserDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api
@RestController
@RequestMapping("/api/users")
public class UserContoller {
    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "Create user by providing name, email and password")
    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public void create(@RequestBody UserForm userForm) throws ApiException {
        userDto.create(userForm);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody LoginForm form, HttpServletResponse response) throws ApiException {
        String accessToken = userDto.login(form);
        Cookie jwtCookie = new Cookie("access_token", accessToken);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(30 * 24 * 60 * 60);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie newCookie = new Cookie("access_token", null);
        newCookie.setHttpOnly(true);
        newCookie.setPath("/");
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);
        response.sendRedirect("/user/login");
    }


}
