package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.domain.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    @Insert("INSERT INTO order_item(amount,symbol,order_type,status,buy_price,sell_price,buy_time,t) " +
            "VALUES(#{amount},#{symbol},#{orderType},#{status},#{buyPrice},#{sellPrice},#{buyTime},#{t})")
    void insert(Order order);

    @Select("select *,order_item_id as orderItemId, buy_time as buyTime, order_id as orderId, buy_price as buyPrice, sell_price as sellPrice " +
            "from order_item " +
            "where status = #{status}")
    List<Order> selectListByStatus(int status);


    @Delete("delete from order_item " +
            "where order_item_id = #{orderId}")
    void delete(long orderId);

    @Update("update order_item " +
            "set order_id= #{orderId}, buy_price= #{buyPrice}, status= #{status}, sell_price=#{sellPrice} " +
            "where order_item_id = #{orderItemId}")
    void update(Order order);

}
