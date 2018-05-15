package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.mapper.OrderMapper;
import com.cmic.hbservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderItemMapper;

    @Override
    public void addOrder(Order order) {
        order.setOrderType("sell-limit");
        order.setStatus(OrderStatus.Pending);
        order.setSellPrice(0F);
        order.setBuyPrice(0F);
        this.orderItemMapper.insert(order);
    }
}
