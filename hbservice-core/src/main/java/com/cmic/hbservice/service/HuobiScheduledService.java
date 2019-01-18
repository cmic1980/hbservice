package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.huobi.request.CreateOrderRequest;
import com.huobi.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class HuobiScheduledService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HuobiService huobiService;

    @Scheduled(cron = "*/10 * * * * ?")
    public void createOrder() {
        List<Order> orderList = this.orderService.getPendingList();
        orderList.forEach(o -> {
            Date date = new Date();
            if (date.getTime() - o.getBuyTime().getTime() > 0) {

                var buyPrice = this.huobiService.getBuyPrice(o.getSymbol());
                o.setBuyPrice(buyPrice);

                float t = ((100 + o.getT()) / 100);
                BigDecimal sellPrice = buyPrice.multiply(new BigDecimal(t));
                o.setSellPrice(sellPrice);

                this.huobiService.createBuyOrder(o);
                this.orderService.updateOrder(o);
            }
        });
    }


    /**
     * 1. 检查挂单成交状态
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void sellOrder() {
        var itemList = this.huobiService.getBalanceItemList();
        var buyingList = this.orderService.getBuyingList();

        // itemList = itemList.stream().filter(s -> s.get("balance").equals("0") == false).collect(Collectors.toList());
        itemList.forEach(o -> {
            String count = o.get("balance").toString();
            String type = o.get("type").toString();

            if (type.equals("trade")) {
                // 找到匹配币种和相应剩余数量
                var buyingOrder = buyingList.stream().filter(s -> {
                    String symbol = s.getSymbol();
                    symbol = symbol.replace("eth", "");
                    String currency = o.get("currency").toString();
                    return symbol.equals(currency);
                }).findAny();

                if (buyingOrder.isPresent()) {
                    Order order = buyingOrder.get();
                    order.setAmount(new BigDecimal(count));

                    var ethCount = order.getAmount().multiply(order.getSellPrice()) ;
                    if (ethCount.doubleValue() > 0.0001) {
                        this.huobiService.createSellOrder(order);
                    }
                }
            }
        });
    }

    /**
     * 1. 取消订单
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void cancelOrder() {
        var buyingList = this.orderService.getBuyingList();

        buyingList.stream().forEach(o -> {
            var orderId = o.getOrderId();
            // 撤销一小时后的未成交订单 （如果部分成交或全部成交调用撤销函数不会有错误，所以没判断是否成交，就是直接撤销所以买单）
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(o.getBuyTime());
            calendar.add(Calendar.HOUR, 1);
            Date sellDate = calendar.getTime();

            if (date.getTime() - sellDate.getTime() > 0) {
                this.huobiService.cancelOrder(orderId);
                o.setStatus(OrderStatus.Done);
                this.orderService.updateOrder(o);
            }
        });
    }


}
