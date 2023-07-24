package com.study.shopping.mapper;

import com.study.shopping.pojo.Goods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GoodsMapper {
    /**
     *
     * @param goodsName 商品名称
     * @param producer 商品生产厂家
     * @param startPrice 查询的最低价格
     * @param endPrice 查询的最高价格
     * @param order 展示顺序：0按照生产日期升序显示，1按照生产日期降序排序
     * @param startProduceTime 查询生产日期最早时间
     * @param endProduceTime 查询生产日期最晚时间
     * @return 查询到的所有结果
     */
    List<Goods> list(String goodsName, String producer, Double startPrice, Double endPrice,
                     Integer order,LocalDate startProduceTime, LocalDate endProduceTime);

    /**
     * 添加商品
     * @param goods 需要添加的商品信息
     */
    @Insert("insert into shopping.goods (goods_name, producer, produce_date, type, buying_price, price,inventory) VALUES " +
            "(#{goodsName},#{producer},#{produceDate},#{type},#{buyingPrice},#{price},#{inventory})")
    void add(Goods goods);

    /**
     * 通过id查询商品
     * @param id 需要查询的商品id
     * @return 返回查询结果
     */
    @Select("select * from shopping.goods where goods_id=#{id}")
    Goods getById(Integer id);

    void update(Goods goods);

    void delete(List<Integer> ids);
}
