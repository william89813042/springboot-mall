package com.systex.william.springbootmall.dao.impl;

import com.systex.william.springbootmall.dao.ProductDao;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Product getProductById(Integer productId) {
        /* 如果選擇 NamedParameterJdbcTemplate，則使用 :productId 來進行參數傳遞；如果選擇傳統 SQL 問題，則使用 ?。 */
        String sql = """
                SELECT * FROM product
                WHERE product_id = :productId;
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        /*
          這裡的 query() 方法會回傳一個 List<Product> 物件，但是我們的 getProductById() 方法的回傳型別是 Product，
          所以這裡應該要取出 List<Product> 中的第一個元素，然後回傳。
         */
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        /*
         * 如果 productList 的大小大於 0，則回傳第一個元素，否則回傳 null。
         */
        if (!productList.isEmpty()) {
            return productList.get(0);
        } else {
            return null;
        }

    }
}
