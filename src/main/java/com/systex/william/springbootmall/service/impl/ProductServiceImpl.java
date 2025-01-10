package com.systex.william.springbootmall.service.impl;

import com.systex.william.springbootmall.dao.ProductDao;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
