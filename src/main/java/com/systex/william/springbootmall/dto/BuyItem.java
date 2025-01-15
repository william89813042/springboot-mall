package com.systex.william.springbootmall.dto;


import jakarta.validation.constraints.NotNull;

public class BuyItem {

/* 對應前端傳來的key */
    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
