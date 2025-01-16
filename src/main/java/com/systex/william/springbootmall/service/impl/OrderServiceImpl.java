package com.systex.william.springbootmall.service.impl;


import com.systex.william.springbootmall.dao.OrderDao;
import com.systex.william.springbootmall.dao.ProductDao;
import com.systex.william.springbootmall.dao.UserDao;
import com.systex.william.springbootmall.dto.BuyItem;
import com.systex.william.springbootmall.dto.CreateOrderRequest;
import com.systex.william.springbootmall.model.Order;
import com.systex.william.springbootmall.model.OrderItem;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.model.User;
import com.systex.william.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final UserDao userDao;

    public OrderServiceImpl(OrderDao orderDao,
                            ProductDao productDao,
                            UserDao userDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.userDao = userDao;
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

        // 檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            logger.warn("該 userId {} 不存在" , userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // 檢查 product 是否存在
            if (product == null) {
                logger.warn("該商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                logger.warn("該商品 {} 的庫存數量不足，無法購買，剩餘庫存 {} ，欲購買數量 {}", buyItem.getProductId() , product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productDao.updateStock(buyItem.getProductId(), product.getStock() - buyItem.getQuantity());

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
