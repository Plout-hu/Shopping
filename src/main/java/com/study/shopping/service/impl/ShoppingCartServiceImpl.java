package com.study.shopping.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.study.shopping.mapper.CustomerMapper;
import com.study.shopping.mapper.GoodsMapper;
import com.study.shopping.mapper.OrderMapper;
import com.study.shopping.mapper.ShoppingCartMapper;
import com.study.shopping.pojo.*;
import com.study.shopping.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void add(ShoppingCart shoppingCart) {
        Goods goods = goodsMapper.getById(shoppingCart.getGoodsId());
        shoppingCart.setGoodsName(goods.getGoodsName());
        shoppingCart.setGoodsPrice(goods.getPrice());
        shoppingCart.setGoodsBuyingPrice(goods.getBuyingPrice());
        shoppingCart.setOrderTime(LocalDateTime.now());
        shoppingCartMapper.add(shoppingCart);
        int inventory = goods.getInventory() - shoppingCart.getAmount();
        if (inventory > 0) {
            goods.setInventory(inventory);
            goodsMapper.update(goods);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(goods.getGoodsId());
            goodsMapper.delete(list);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getByGoodsIdAndUserId(ids);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Goods temp = goodsMapper.getById(shoppingCart.getGoodsId());
            temp.setInventory(temp.getInventory() + shoppingCart.getAmount());
            goodsMapper.update(temp);
        }
        shoppingCartMapper.delete(ids);
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        if (shoppingCart.getAmount() <= 0) {
            List<Integer> ids = new ArrayList<>();
            ids.add(shoppingCart.getId());
            shoppingCartMapper.delete(ids);
            return;
        }
        shoppingCartMapper.update(shoppingCart);
    }

    @Override
    public String buy(Customer customer) {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(customer.getUserId(), null);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            if (goodsMapper.getById(shoppingCart.getGoodsId()).getInventory() - shoppingCart.getAmount() < 0) {
                return "库存不足，无法购买";
            }
        }
        LocalDateTime now = LocalDateTime.now();
        List<Integer> list=new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
//            Goods goods = goodsMapper.getById(shoppingCart.getGoodsId());
            Order order = new Order(0, customer.getUserId(), shoppingCart.getGoodsId(), customer.getUsername(),
                    shoppingCart.getGoodsName(), shoppingCart.getGoodsPrice(), shoppingCart.getAmount(), now);
            orderMapper.add(order);
            list.add(shoppingCart.getId());
        }
        shoppingCartMapper.delete(list);
        return null;
    }

    @Override
    public PageBean list(Integer page, Integer pageSize, Integer userId, String goodsName) {
        PageHelper.startPage(page, pageSize);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(userId, goodsName);
        Page<ShoppingCart> shoppingCartPage = (Page<ShoppingCart>) shoppingCartList;
        return new PageBean(shoppingCartPage.getTotal(), shoppingCartPage.getResult());
    }
}
