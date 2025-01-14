package com.systex.william.springbootmall.service.impl;

import com.systex.william.springbootmall.dao.UserDao;
import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;
import com.systex.william.springbootmall.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
