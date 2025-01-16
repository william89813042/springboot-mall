package com.systex.william.springbootmall.service.impl;


import com.systex.william.springbootmall.dao.OrderDao;
import com.systex.william.springbootmall.dao.ProductDao;
import com.systex.william.springbootmall.dto.BuyItem;
import com.systex.william.springbootmall.dto.CreateOrderRequest;
import com.systex.william.springbootmall.model.Order;
import com.systex.william.springbootmall.model.OrderItem;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final ProductDao productDao;

    public OrderServiceImpl(OrderDao orderDao,
                            ProductDao productDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }


    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // 計算總金額
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;

    }
}
