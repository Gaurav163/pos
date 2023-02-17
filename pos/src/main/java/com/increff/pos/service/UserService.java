package com.increff.pos.service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${supervisors.email}")
    private String supervisorEmail;

    public User getByParameter(String name, String value) {
        return userDao.getByParameter(name, value);
    }

    public void create(User user) throws ApiException {
        User existingUser = userDao.getByParameter("email", user.getEmail());
        if (existingUser != null) {
            throw new ApiException("Email already used");
        }
        List<String> supervisorEmails = Arrays.asList(supervisorEmail.split(","));
        if (supervisorEmails.contains(user.getEmail())) {
            user.setRole("supervisor");
        } else {
            user.setRole("operator");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.create(user);
    }


}
