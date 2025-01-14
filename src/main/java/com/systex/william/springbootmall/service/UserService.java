package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.UserLoginRequest;
import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;
import jakarta.validation.Valid;

public interface UserService {

    User getUserById(Integer userId);

    Integer register(UserRegisterRequest userRegisterRequest);

    User login(@Valid UserLoginRequest userLoginRequest);
}
