package com.study.shopping.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.study.shopping.mapper.OrderMapper;
import com.study.shopping.pojo.Goods;
import com.study.shopping.pojo.Order;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Override
    public PageBean list(Integer page, Integer pageSize, String username, LocalDate start, LocalDate end) {
        PageHelper.startPage(page, pageSize);
        List<Order> orderList = orderMapper.list(username, start, end);
        Page<Order> orderPage = (Page<Order>) orderList;
        return new PageBean(orderPage.getTotal(), orderPage.getResult());
    }
}
