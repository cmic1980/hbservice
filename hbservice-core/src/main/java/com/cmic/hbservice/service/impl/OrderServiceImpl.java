package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.cmic.hbservice.mapper.AnalysisResultMapper;
import com.cmic.hbservice.mapper.OrderMapper;
import com.cmic.hbservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    private List<Order> orderList;

    private List<Order> buyingList;

    @Override
    public void addOrder(Order order) {
        this.orderList = null;
        order.setOrderType("buy-limit");
        order.setStatus(OrderStatus.Pending);
        order.setSellPrice(BigDecimal.ZERO);
        order.setBuyPrice(BigDecimal.ZERO);
        this.orderMapper.insert(order);

        this.clearOrderListCache();
    }

    @Override
    public List<Order> getPendingList() {
        orderList = this.orderMapper.selectListByStatus(OrderStatus.Pending);
        return orderList;
    }

    @Override
    public void cancelOrder(long orderItemId) {
        this.orderMapper.delete(orderItemId);
        this.clearOrderListCache();
    }

    @Override
    public void updateOrder(Order order) {
        this.orderMapper.update(order);
        this.clearOrderListCache();
    }

    @Override
    public List<Order> getBuyingList() {
        buyingList = this.orderMapper.selectListByStatus(OrderStatus.Buy);
        return buyingList;
    }

    private void clearOrderListCache() {
        this.orderList = null;
        this.buyingList = null;
    }
}
