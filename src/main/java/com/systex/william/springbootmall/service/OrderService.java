package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.CreateOrderRequest;
import com.systex.william.springbootmall.model.Order;

public interface OrderService {
    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId,  CreateOrderRequest createOrderRequest);

}
