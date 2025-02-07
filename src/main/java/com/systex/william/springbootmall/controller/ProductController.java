package com.systex.william.springbootmall.controller;

import com.systex.william.springbootmall.constant.ProductCategory;
import com.systex.william.springbootmall.dto.ProductQueryParams;
import com.systex.william.springbootmall.dto.ProductRequest;
import com.systex.william.springbootmall.model.Product;
import com.systex.william.springbootmall.service.ProductService;
import com.systex.william.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 取得所有商品的數據
     */
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
            /* 查詢條件 Filtering */
            @RequestParam (required = false) ProductCategory category, /* (required = false)這個參數是可選的，如果前端沒有傳這個參數的話，那就會是 null */
            @RequestParam (required = false) String search,
            /* 排序 Sorting */
            @RequestParam (defaultValue = "created_date") String orderBy, /* 要去根據甚麼欄位來做排序 ， 預設是 created_date */
            @RequestParam (defaultValue = "desc") String sort, /* 是要做升冪還是降冪的排序 ， 預設是 desc */
            /* 分頁 Pagination */
            @RequestParam (defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam (defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        // 取得 product list
        List<Product> productList = productService.getProducts(productQueryParams);

        // 取得 product 總數
        Integer total = productService.countProduct(productQueryParams);

        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);
        /** 把這個商品的數據放在 response body 裡面回傳給前端 */
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    /**
     * 根據商品的 id 去取得商品的數據
     */
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

    /**
     * 新增 product 的數據
     */
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    /**
     * 修改 product 的數據
     */
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        /** 查詢 product 是否存在 */
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        /** 修改 product 的數據 */
        productService.updateProduct(productId, productRequest);

        Product updatedProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    /**
     * 刪除 product 的數據
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
