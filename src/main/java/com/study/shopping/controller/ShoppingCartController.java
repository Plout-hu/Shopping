package com.study.shopping.controller;

import com.study.shopping.pojo.*;
import com.study.shopping.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/shoppingcart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 向购物车中添加商品
     *
     * @param shoppingCart 传递需要添加的商品相关信息
     * @return 返回添加成功回执
     */
    @PostMapping
    public Result add(@RequestBody ShoppingCart shoppingCart) {
        log.info("向购物车添加商品：{}", shoppingCart);
        shoppingCartService.add(shoppingCart);
        return Result.success();
    }

    /**
     * 批量删除购物车中的商品
     * @param ids 订单id
     * @return 返回成功回执
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("批量删除购物车:{}",ids);
        shoppingCartService.delete(ids);
        return Result.success();
    }

    /**
     * 更新购物车信息
     *
     * @param shoppingCart 需要更新的相关信息
     * @return 返回成功回执
     */
    @PutMapping
    public Result update(@RequestBody ShoppingCart shoppingCart) {
        log.info("更新购物车：{}", shoppingCart);
        shoppingCartService.update(shoppingCart);
        return Result.success();
    }

    /**
     * 分页查询购物车
     *
     * @param page      第几页
     * @param pageSize  一页数据个数
     * @param userId    用户id
     * @param goodsName 商品名称
     * @return 返回成功回执
     */
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "100") Integer pageSize,
                       Integer userId, String goodsName) {
        log.info("分页查询购物车信息:{},{},{},{}", page, pageSize, userId, goodsName);
        PageBean pageBean = shoppingCartService.list(page, pageSize, userId, goodsName);
        return Result.success(pageBean);
    }

    /**
     * 购买商品
     *
     * @param customer 需要购买商品的用户
     * @return 返回购买是否成功
     */
    @PostMapping("/buy")
    public Result buy(@RequestBody Customer customer) {
        log.info("提交购物订单，清空购物车:{}",customer);
        String message = shoppingCartService.buy(customer);
        if (message == null) {
            return Result.success();
        } else {
            return Result.error(message);
        }
    }

}
