package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.dao.UserDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;

public class UserServiceTest extends AbstractUnitTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private Helper helper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreate() throws ApiException {
        User user = helper.getUser("name", "email", "password", null);
        userService.create(user);
        User savedUser = userDao.getByParameter("email", "email");
        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals("operator", savedUser.getRole());
        assertNotEquals("password", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("password", savedUser.getPassword()));
    }

    @Test(expected = ApiException.class)
    public void testCreateDuplicateEmail() throws ApiException {
        User user = helper.getUser("name", "email", "password", null);
        User user1 = helper.getUser("name2", "email", "passwordss", null);
        userService.create(user);
        userService.create(user1);
        User savedUser = userDao.getByParameter("email", "email");
    }

    @Test
    public void testGetByParameter() {
        User user = helper.createUser("name", "email", "password", "operator");
        User savedUser = userService.getByParameter("email", "email");
        User dummyUser = userService.getByParameter("email", "dummy");
        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getRole(), savedUser.getRole());
        assertNull(dummyUser);
    }
}
