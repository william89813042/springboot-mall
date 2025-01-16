package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.CreateOrderRequest;
import com.systex.william.springbootmall.dto.OrderQueryParams;
import com.systex.william.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer countOrder(OrderQueryParams orderQueryParams);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId,  CreateOrderRequest createOrderRequest);


}
