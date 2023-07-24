package com.study.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private int customerId;
    private int goodsId;
    private String customerName;
    private String goodsName;
    private float price;
    private int goodsNumber;
    private LocalDateTime orderTime;
}
