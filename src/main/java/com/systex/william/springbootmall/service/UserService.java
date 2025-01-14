package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);

}
