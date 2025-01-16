package com.systex.william.springbootmall.dao.impl;

import com.systex.william.springbootmall.dao.OrderDao;

import com.systex.william.springbootmall.dto.OrderQueryParams;
import com.systex.william.springbootmall.model.Order;
import com.systex.william.springbootmall.model.OrderItem;
import com.systex.william.springbootmall.rowmapper.OrderItemRowMapper;
import com.systex.william.springbootmall.rowmapper.OrderRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = """
                SELECT count(*)
                FROM `order`
                WHERE 1=1
                """;
        Map<String, Object> map = new HashMap<>(); // <key, value >

        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 在最後一個參數這裡就要去填上 Integer.class那表示說我們要將 count 的值去轉換成是一個 Integer 類型的返回值
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = """
                SELECT *
                FROM `order`
                WHERE 1=1
                """;

        Map<String, Object> map = new HashMap<>(); // <key, value >

        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 排序 新的訂單擺前，舊的訂單擺後
        // 後端寫死原因，為了不要讓前端去改變訂單的排序紀錄
        sql += " ORDER BY  created_date DESC ";

        // 分頁
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper()); // List<Order>

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = """
                SELECT *
                FROM `order`
                WHERE order_id = :orderId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper()); // List<Order>

        if (orderList.size() > 0 ) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = """
                SELECT *
                FROM order_item
                LEFT JOIN product as p ON order_item.product_id = p.product_id
                WHERE order_item.order_id = :orderId
                """;

                Map<String, Object> map = new HashMap<>(); // <key, value >
                map.put("orderId", orderId);

                List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper()); // List<OrderItem >

                return orderItemList;
    }


    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = """
                INSERT INTO `order`(user_id, total_amount, created_date, last_modified_date)
                VALUES(:userId, :totalAmount, :createdDate, :lastModifiedDate)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);


        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {

//        // 法一：使用 for loop 逐筆加入，效率較低
//        for (OrderItem orderItem : orderItemList) {
//            String sql = """
//                    INSERT INTO order_item(order_id, product_id, quantity, amount)
//                    VALUES(:orderId, :productId, :quantity, :amount)
//                    """;
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderId", orderId);
//            map.put("productId", orderItem.getProductId());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//
//            namedParameterJdbcTemplate.update(sql, map);
//        }

        // 法二：batch update 適用於一次性加入多筆數據，效率較高
        String sql = """
                INSERT INTO order_item(order_id, product_id, quantity, amount)
                VALUES(:orderId, :productId, :quantity, :amount)
                """;

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId", orderId);
            parameterSources[i].addValue("productId", orderItem.getProductId());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {
        if (orderQueryParams.getUserId() != null) {
            sql += " AND user_id = :userId";
            map.put("userId", orderQueryParams.getUserId());
        }
        return sql;
    }

}

