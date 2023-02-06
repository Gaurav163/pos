package com.increff.pos.dao;

import com.increff.pos.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao<User> {

    protected UserDao() {
        super(User.class);
    }
}
