package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;
import com.huobi.response.IntrustDetail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface HuobiService {
    Integer getAccountId();
    BigDecimal getBuyPrice(String symbol);
    long createBuyOrder(Order order);
    long createSellOrder(Order order);
    void cancelOrder(Long orderId);
    List<HashMap> getBalanceItemList();
    List<IntrustDetail> getOrderList(String symbol);
}
