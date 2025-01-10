package com.systex.william.springbootmall.dao;

import com.systex.william.springbootmall.dto.ProductRequest;
import com.systex.william.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
