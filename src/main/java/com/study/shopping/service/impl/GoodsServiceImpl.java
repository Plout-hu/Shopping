package com.study.shopping.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.study.shopping.mapper.GoodsMapper;
import com.study.shopping.pojo.Goods;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public PageBean list(Integer page, Integer pageSize, String goodsName, String producer, Double startPrice,
                         Double endPrice,Integer order, LocalDate startProduceTime, LocalDate endProduceTime) {
        PageHelper.startPage(page,pageSize);
        List<Goods> goodsList=goodsMapper.list(goodsName,producer,startPrice,endPrice,order,startProduceTime,endProduceTime);
        Page<Goods> goodsPage=(Page<Goods>) goodsList;
        return new PageBean(goodsPage.getTotal(),goodsPage.getResult());
    }

    @Override
    public void save(Goods goods) {
        goodsMapper.add(goods);
    }

    @Override
    public Goods getById(Integer id) {
        return goodsMapper.getById(id);
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.update(goods);
    }

    @Override
    public void delete(List<Integer> ids) {
        goodsMapper.delete(ids);
    }

}
