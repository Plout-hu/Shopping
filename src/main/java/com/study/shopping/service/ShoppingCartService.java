package com.study.shopping.service;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.pojo.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void add(ShoppingCart shoppingCart);

    void delete(List<Integer> ids);

    void update(ShoppingCart shoppingCart);

    String buy(Customer customer);

    PageBean list(Integer page, Integer pageSize, Integer userId, String goodsName);
}
