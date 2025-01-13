package com.systex.william.springbootmall.dto;

import com.systex.william.springbootmall.constant.ProductCategory;

public class ProductQueryParms {

    private ProductCategory category;

    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }
}
