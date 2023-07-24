package com.study.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    private int id;
    private int userId;
    private int goodsId;
    private String goodsName;
    private float goodsPrice;
    private float goodsBuyingPrice;
    private int amount;
    private LocalDateTime orderTime;
}
