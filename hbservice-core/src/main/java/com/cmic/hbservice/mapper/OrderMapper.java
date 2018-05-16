package com.cmic.hbservice.mapper;

import com.cmic.hbservice.domain.AnalysisResult;
import com.cmic.hbservice.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO order_item(amount,symbol,order_type,status,buy_price,sell_price,buy_time,t) " +
            "VALUES(#{amount},#{symbol},#{orderType},#{status},#{buyPrice},#{sellPrice},#{buyTime},#{t})")
    void insert(Order order);

    @Select("select * " +
            "from order_item " +
            "where status = 1")
    List<Order> selectListByStatus(int status);
}
