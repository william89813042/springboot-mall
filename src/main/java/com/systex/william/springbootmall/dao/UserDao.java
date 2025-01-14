package com.systex.william.springbootmall.dao;

import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userid);

    Integer createUser(UserRegisterRequest userRegisterRequest);

}
