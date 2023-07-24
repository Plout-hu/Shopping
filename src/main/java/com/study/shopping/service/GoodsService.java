package com.study.shopping.service;

import com.study.shopping.pojo.Goods;
import com.study.shopping.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

public interface GoodsService {
    PageBean list(Integer page, Integer pageSize, String goodsName, String producer, Double startPrice, Double endPrice,
                  Integer order,LocalDate startProduceTime, LocalDate endProduceTime);

    void save(Goods goods);

    Goods getById(Integer id);

    void update(Goods goods);

    void delete(List<Integer> ids);
}
