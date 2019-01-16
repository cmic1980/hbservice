package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.mapper.OrderMapper;
import com.cmic.hbservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    private List<Order> orderList;

    @Override
    public void addOrder(Order order) {
        this.orderList = null;
        order.setOrderType("sell-limit");
        order.setStatus(OrderStatus.Pending);
        order.setSellPrice(0D);
        order.setBuyPrice(0D);
        this.orderMapper.insert(order);
    }

    @Override
    public List<Order> getPendingList() {
        if (this.orderList == null) {
            orderList = this.orderMapper.selectListByStatus(OrderStatus.Pending);
        }
        return orderList;
    }

    @Override
    public void cancelOrder(long orderId) {
        this.orderList = null;
        this.orderMapper.delete(orderId);
    }
}
