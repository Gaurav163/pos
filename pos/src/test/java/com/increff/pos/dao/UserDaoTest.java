package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDaoTest extends AbstractUnitTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        User user = testHelper.getUser("name", "email", "password", "operator");
        userDao.create(user);
        User savedUser = userDao.getByParameter("email", "email");
        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRole(), savedUser.getRole());
    }

    @Test(expected = PersistenceException.class)
    public void testCreateDuplicate() {
        User user = testHelper.getUser("name", "email", "password", "operator");
        User user1 = testHelper.getUser("name", "email", "password", "operator");
        userDao.create(user);
        userDao.create(user1);
        userDao.getByParameter("email", "email");
    }
}
