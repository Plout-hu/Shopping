package com.study.shopping.controller;

import com.study.shopping.pojo.Goods;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.pojo.Result;
import com.study.shopping.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询所有商品信息
     * @param page 页码
     * @param pageSize 一页数据个数
     * @param goodsName 商品名称
     * @param producer 商品生产厂家
     * @param startPrice 查询的最低价格
     * @param endPrice 查询的最高价格
     * @param order 展示顺序：0按照生产日期升序显示，1按照生产日期降序排序
     * @param startProduceTime 查询生产日期最早时间
     * @param endProduceTime 查询生产日期最晚时间
     * @return 查询到的所有结果
     */
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1")Integer page,
                       @RequestParam(defaultValue = "5")Integer pageSize,
                       String goodsName, String producer, Double startPrice, Double endPrice,
                       @RequestParam(defaultValue = "0") Integer order,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate startProduceTime,
                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate endProduceTime){
        log.info("查询商品数据：{}，{}，{}，{}，{},{}，{}，{}，{}",page,pageSize,goodsName,producer,startPrice,endPrice,order,
                startProduceTime,endProduceTime);
        PageBean pageBean= goodsService.list(page,pageSize,goodsName,producer,startPrice,endPrice,order,startProduceTime,
                endProduceTime);
        return Result.success(pageBean);
    }

    /**
     * 添加商品
     * @param goods 需要添加的商品的信息
     * @return 返回添加成功信息
     */
    @PostMapping
    public Result save(@RequestBody Goods goods){
        log.info("添加商品:{}",goods);
        goodsService.save(goods);
        return Result.success();
    }

    /**
     * 通过Id查询商品
     * @param id，需要插叙的商品id
     * @return 查询商品结果
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("通过Id查询用户:{}",id);
        Goods goods= goodsService.getById(id);
        return Result.success(goods);
    }

    /**
     * 更新商品信息
     * @param goods 需要更新的商品相关信息
     * @return 返回更新成功回执
     */
    @PutMapping
    public Result update(@RequestBody Goods goods){
        log.info("更新商品信息");
        goodsService.update(goods);
        return Result.success();
    }

    /**
     * 批量删除商品
     * @param ids 需要删除的商品id
     * @return 返回删除成功回执
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("批量删除商品，参数:{}",ids);
        goodsService.delete(ids);
        return Result.success();
    }
}
