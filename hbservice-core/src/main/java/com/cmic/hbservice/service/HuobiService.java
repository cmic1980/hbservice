package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;

public interface HuobiService {
    Integer getAccountId();
    Double getBuyPrice(String symbol);
    long createBuyOrder(Order order);
    long createSellOrder(Order order);
    void cancelOrder(Long orderId);
}
