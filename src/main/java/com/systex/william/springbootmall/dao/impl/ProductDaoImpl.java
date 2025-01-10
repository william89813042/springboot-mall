package com.systex.william.springbootmall.dao.impl;

import com.systex.william.springbootmall.dao.ProductDao;
import com.systex.william.springbootmall.dto.ProductRequest;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.rowmapper.ProductRowMapper;
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
public class ProductDaoImpl implements ProductDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Product> getProducts() {
        String sql = """
                SELECT * FROM product;
                """;

        Map<String, Object> map = new HashMap<>(); // <key, value>

        List<Product> productList =  namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;

        /** 3435也可以寫 */
        // return namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = """
                INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)
                VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = """
                UPDATE product
                SET product_name = :productName,
                    category = :category,
                    image_url = :imageUrl,
                    price = :price,
                    stock = :stock,
                    description = :description,
                    last_modified_date = :lastModifiedDate
                WHERE product_id = :productId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = """
                DELETE FROM product
                WHERE product_id = :productId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
