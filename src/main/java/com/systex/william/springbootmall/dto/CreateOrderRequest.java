package com.systex.william.springbootmall.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {

    @NotEmpty //加在Map或List上，表示不為null且size>0
    private List<BuyItem> buyItemList; //buyItemList 要去對應前端傳來的值

    public List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
