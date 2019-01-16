package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.domain.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    @Insert("INSERT INTO order_item(amount,symbol,order_type,status,buy_price,sell_price,buy_time,t) " +
            "VALUES(#{amount},#{symbol},#{orderType},#{status},#{buyPrice},#{sellPrice},#{buyTime},#{t})")
    void insert(Order order);

    @Select("select *,order_item_id as orderItemId " +
            "from order_item " +
            "where status = #{status}")
    List<Order> selectListByStatus(int status);


    @Delete("delete from order_item " +
            "where order_item_id = #{orderId}")
    void delete(int orderId);
}
