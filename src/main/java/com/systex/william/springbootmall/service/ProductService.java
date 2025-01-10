package com.systex.william.springbootmall.service;

import com.systex.william.springbootmall.dto.ProductRequest;
import com.systex.william.springbootmall.model.Product;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();
    /** 那表示這個 ProductService 提供了一個功能，就是可以去根據 product 的 id去取得商品的數 */
    Product getProductById(Integer productId);

    Integer createProduct(@Valid ProductRequest productRequest);

    void updateProduct(Integer productId, @Valid ProductRequest productRequest);

    void deleteProductById(Integer productId);

}
