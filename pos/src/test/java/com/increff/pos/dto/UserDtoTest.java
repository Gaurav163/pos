package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.UserDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertNotNull;

public class UserDtoTest extends AbstractUnitTest {
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private UserDto userDto;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreate() throws ApiException {
        UserForm form = testHelper.createUserForm("name", "email@g", "password");
        userDto.create(form);
        User user = userDao.getByParameter("email", "email@g");
        assertNotNull(user);
    }

    @Test
    public void testLogin() throws ApiException {
        User user = testHelper.createUser("name", "email@g", passwordEncoder.encode("password@"), "operator");
        LoginForm form = testHelper.createLoginForm("email@g", "password@");
        String token = userDto.login(form);
        assertNotNull(token);
    }

    @Test(expected = ApiException.class)
    public void testLoginInvalidUser() throws ApiException {
        User user = testHelper.createUser("name", "email@g", passwordEncoder.encode("password@"), "operator");
        LoginForm form = testHelper.createLoginForm("email@invalid", "password@");
        userDto.login(form);
    }

    @Test(expected = ApiException.class)
    public void testLoginWrongPassword() throws ApiException {
        User user = testHelper.createUser("name", "email@g", passwordEncoder.encode("password@"), "operator");
        LoginForm form = testHelper.createLoginForm("email@", "password@worng");
        userDto.login(form);
    }
}
