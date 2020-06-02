package com.itheima.travel.service;

import com.itheima.travel.entity.User;
import com.itheima.travel.exception.CustomerErrorMsgException;

public interface IUserService {
    User login(String username, String password) throws CustomerErrorMsgException;

    User findUserByUsername(String username);

    int register(User user);
}
