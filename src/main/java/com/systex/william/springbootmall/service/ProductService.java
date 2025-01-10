package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.model.Product;

public interface ProductService {

    /**
     * 那表示這個 ProductService 提供了一個功能，就是可以去根據 product 的 id去取得商品的數據
     */
    Product getProductById(Integer productId);
}
