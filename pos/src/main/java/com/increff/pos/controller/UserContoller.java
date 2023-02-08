package com.increff.pos.controller;


import com.increff.pos.dto.UserDto;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
