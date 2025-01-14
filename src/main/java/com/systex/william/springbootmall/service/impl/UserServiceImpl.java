package com.systex.william.springbootmall.service.impl;

import com.systex.william.springbootmall.dao.UserDao;
import com.systex.william.springbootmall.dto.UserLoginRequest;
import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;
import com.systex.william.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;


/*
記得一個原則
就是我們可以在 Service 層去添加許多的 if-else 的判斷邏輯
那但是我們不能夠在 Dao 層去添加這些判斷邏輯
 */
@Component
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    // 註冊
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查 email 是否已被註冊
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            logger.warn("該 email {} 已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    // 登入
    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());


        // 檢查 email 是否已被註冊
        if (user == null) {
            logger.warn("該 email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        userLoginRequest.setPassword(hashedPassword);

        // 檢查密碼是否正確
        if (user.getPassword().equals(hashedPassword)) {  // equals() 用來比較兩個String是否相等
            return user;
        } else {
            logger.warn("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
