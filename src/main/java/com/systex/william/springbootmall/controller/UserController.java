package com.systex.william.springbootmall.controller;

import com.systex.william.springbootmall.dto.UserRegisterRequest;
import com.systex.william.springbootmall.model.User;
import com.systex.william.springbootmall.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {  // register() 因為要去接住前端傳過來的request body 的參數
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.getUserById(userId); //我們可以去根據傳進去的 userId 的參數去資料庫中查詢這一筆 user 的 數據出來

        return ResponseEntity.status(HttpStatus.CREATED).body(user); // 回傳一個 ResponseEntity 物件，這個物件的狀態碼是 HttpStatus.CREATED，而且裡面的 body 是 user 物件
    }
}
