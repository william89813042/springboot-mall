package com.systex.william.springbootmall.controller;

import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        /*
        當前端來請求這個 url 路徑，那我們就會去透過 productService 的 getProductById 方法去資料庫中去查詢這一筆商品的數據出來
        那如果這個查詢出來的商品數據，他的值不是 null 的話，那就表示有找到這一筆商品的數據
         */
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
