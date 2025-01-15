package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.CreateOrderRequest;

public interface OrderService {
    Integer createOrder(Integer userId,  CreateOrderRequest createOrderRequest);

}
