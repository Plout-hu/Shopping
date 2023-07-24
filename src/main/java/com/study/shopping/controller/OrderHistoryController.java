package com.study.shopping.controller;

import com.study.shopping.pojo.PageBean;
import com.study.shopping.pojo.Result;
import com.study.shopping.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin
public class OrderHistoryController {
    @Autowired
    OrderService orderService;

    @GetMapping("/OrderHistory")
    public Result getOrderList(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "5") Integer pageSize,
                               String username,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取历史订单:{},{},{},{},{}", page, pageSize, username, start, end);
        PageBean pageBean = orderService.list(page, pageSize, username, start, end);
        return Result.success(pageBean);
    }
}
