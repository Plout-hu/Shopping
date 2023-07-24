package com.study.shopping.service;

import com.study.shopping.pojo.PageBean;

import java.time.LocalDate;

public interface OrderService {
    PageBean list(Integer page, Integer pageSize, String username, LocalDate start, LocalDate end);
}
