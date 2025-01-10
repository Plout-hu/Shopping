package com.study.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private int goodsId;
    private String goodsName;
    private String producer;
    private String picture;
    private LocalDate produceDate;
    private int type;
    private float buyingPrice;
    private float price;
    private int inventory;
}
