package com.lamfire.jmongo.test.dao;

import com.lamfire.jmongo.dao.DAOSupport;
import com.lamfire.jmongo.test.entity.User;

/**
 * Created by linfan on 2017/5/4.
 */
public class UserDAO extends DAOSupport<User,String> {

    public UserDAO() {
        super("db0","user","USER",User.class);
    }
}
