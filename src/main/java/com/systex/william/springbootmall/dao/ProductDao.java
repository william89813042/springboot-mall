package com.systex.william.springbootmall.dao;

import com.systex.william.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
