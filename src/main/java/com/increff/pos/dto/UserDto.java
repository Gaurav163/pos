package com.increff.pos.dto;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.User;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.increff.pos.util.FormUtil.normalizeForm;
import static com.increff.pos.util.FormUtil.validateForm;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class UserDto {
    @Autowired
    private UserService userService;

    public void create(UserForm form) throws ApiException {
        validateForm(form);
        normalizeForm(form);
        userService.create(mapper(form, User.class));
    }

    public User getByEmail(String email) {
        return userService.getOneByParameter("email", email);
    }

}
