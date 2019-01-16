package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);
    List<Order> getPendingList();
    void cancelOrder(long orderId);
}
