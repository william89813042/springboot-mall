package com.systex.william.springbootmall.rowmapper;

import com.systex.william.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {

    // 對於RowMapper 這個 interface 來說，根本不需要在意這些欄位是從資料庫的那些table出來的 ，rowmapper 提供的的功能是可以讓我們去取的SELECT出來的那一些數據
    @Override
    public OrderItem mapRow(ResultSet rs, int i) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setAmount(rs.getInt("amount"));

        // 這邊是從product table裡面取出來的，可以用來擴充orderItem的數據
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setImageUrl(rs.getString("image_url"));

        return orderItem;
    }

}
