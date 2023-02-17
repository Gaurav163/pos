package com.increff.pos.dto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.User;
import com.increff.pos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.increff.pos.util.FormUtil.validateForm;
import static com.increff.pos.util.MapperUtil.mapper;

@Service
public class UserDto {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(UserForm form) throws ApiException {
        validateForm(form);
        userService.create(mapper(form, User.class));
    }


    public String login(LoginForm form) throws ApiException {
        validateForm(form);
        User user = userService.getByParameter("email", form.getEmail());
        if (user == null) {
            throw new ApiException("User not exist");
        }
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new ApiException("Wrong Password");
        }
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .withClaim("roles", user.getRole())
                .sign(algorithm);
        return accessToken;
    }
}
