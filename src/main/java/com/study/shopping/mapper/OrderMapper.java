package com.study.shopping.mapper;

import com.study.shopping.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into shopping.historical_orders (customer_id, customer_name, goods_id, goods_name, price, " +
            "goods_number, order_time) VALUES (#{customerId},#{customerName},#{goodsId},#{goodsName},#{price},#{goodsNumber},#{orderTime})")
    void add(Order order);

    List<Order> list(String username, LocalDate start, LocalDate end);
}
