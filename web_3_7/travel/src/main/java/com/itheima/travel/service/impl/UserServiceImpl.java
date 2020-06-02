package com.itheima.travel.service.impl;

import com.itheima.travel.dao.IUserDao;
import com.itheima.travel.entity.User;
import com.itheima.travel.exception.CustomerErrorMsgException;
import com.itheima.travel.service.IUserService;
import com.itheima.travel.utils.DaoInstanceFactory;
import com.itheima.travel.utils.Md5Utils;

public class UserServiceImpl implements IUserService {
    private IUserDao userDao = DaoInstanceFactory.getBean(IUserDao.class);

    @Override
    public User login(String username, String password) throws CustomerErrorMsgException {

        String md5 = Md5Utils.getMd5(password);
        User user = userDao.login(username, md5);
        if (user == null) {
            throw new CustomerErrorMsgException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public int register(User user) {
        String password = user.getPassword();
        password = Md5Utils.getMd5(password);
        user.setPassword(password);
        return userDao.addUser(user);
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }
}
