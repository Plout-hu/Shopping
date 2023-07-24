package com.study.shopping.mapper;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    @Insert("insert into shopping.shopping_cart (user_id, goods_id, goods_name, goods_price, goods_buying_price,amount) " +
            "VALUES (#{userId},#{goodsId},#{goodsName},#{goodsPrice},#{goodsBuyingPrice},#{amount}) on DUPLICATE KEY UPDATE amount=#{amount}")
    void add(ShoppingCart shoppingCart);

    void delete(List<Integer> ids);
    @Update("update shopping.shopping_cart set amount=#{amount} where goods_id=#{goodsId} and user_id=#{userId}")
    void update(ShoppingCart shoppingCart);

    List<ShoppingCart> list(Integer userId, String goodsName);

    List<ShoppingCart> getByGoodsIdAndUserId(List<Integer> ids);

    void deleteByGoodsId(List<Integer> goodsIds, int userId);
}
