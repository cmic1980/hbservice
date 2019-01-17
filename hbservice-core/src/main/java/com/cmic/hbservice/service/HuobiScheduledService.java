package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.huobi.request.CreateOrderRequest;
import com.huobi.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HuobiScheduledService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ApiService apiService;

    private Integer accountId;

    @Scheduled(cron = "*/10 * * * * ?")
    public void createOrder() {
        var client = this.apiService.getApiClient();
        List<Order> orderList = this.orderService.getPendingList();
        orderList.forEach(o -> {

            Date date = new Date();
            if (date.getTime() - o.getBuyTime().getTime() > 0) {
                var buyPrice = this.getBuyPrice(o.getSymbol());
                buyPrice = buyPrice / 1.5;

                o.setBuyPrice(buyPrice);
                float t = ((100 + o.getT()) / 100);
                Double sellPrice = buyPrice * t;
                o.setSellPrice(sellPrice);

                this.createBuyOrder(o);
            }
        });
    }

    private Integer getAccountId() {
        if (this.accountId == null) {
            var client = this.apiService.getApiClient();
            AccountsResponse accounts = client.accounts();
            List<Accounts> list = (List<Accounts>) accounts.getData();
            Accounts account = list.get(0);
            this.accountId = account.getId();
        }
        return this.accountId;
    }

    /**
     * 获取最后 小时k 线最后一根的开盘价作为买入价格
     */
    private Double getBuyPrice(String symbol) {
        var client = this.apiService.getApiClient();
        KlineResponse response = client.kline(symbol, "60min", "1");

        var klineList = (ArrayList<Kline>) response.data;
        var kline = klineList.get(0);
        var buyPrice = kline.getOpen();
        return buyPrice;
    }

    private long createBuyOrder(Order order) {
        var client = this.apiService.getApiClient();

        // create order:
        CreateOrderRequest request = new CreateOrderRequest();
        request.accountId = String.valueOf(this.getAccountId());
        request.amount = order.getAmount().toString();
        request.price = order.getBuyPrice().toString();
        if (request.price.length() > 8) {
            request.price = request.price.substring(0, 8);
        }
        request.symbol = order.getSymbol();
        request.type = CreateOrderRequest.OrderType.BUY_LIMIT;
        request.source = "api";


        //------------------------------------------------------ 创建订单  -------------------------------------------------------
        long orderId = client.createOrder(request);
        order.setOrderId(orderId);
        order.setStatus(OrderStatus.Buy);

        this.orderService.updateOrder(order);
        return orderId;
    }

    private long createSellOrder(Order order) {
        var client = this.apiService.getApiClient();

        // create order:
        CreateOrderRequest request = new CreateOrderRequest();
        request.accountId = String.valueOf(this.getAccountId());
        request.amount = order.getAmount().toString();
        request.price = order.getSellPrice().toString();
        if (request.price.length() > 8) {
            request.price = request.price.substring(0, 8);
        }
        request.symbol = order.getSymbol();
        request.type = CreateOrderRequest.OrderType.SELL_LIMIT;
        request.source = "api";


        //------------------------------------------------------ 创建订单  -------------------------------------------------------
        long orderId = client.createOrder(request);
        order.setOrderId(orderId);
        order.setStatus(OrderStatus.Buy);

        this.orderService.updateOrder(order);
        return orderId;
    }

    /**
     * 1. 检查挂单成交状态
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void sellOrder() {
        var client = this.apiService.getApiClient();
        var response = client.balance(this.getAccountId().toString());
        var balance = (Balance) response.getData();

        var buyingList = this.orderService.getBuyingList();

        var itemList = (List<HashMap>) balance.getList();
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
                    order.setAmount(Float.valueOf(count));

                    var ethCount = order.getAmount() * order.getSellPrice();
                    if (ethCount > 0.0001) {
                        this.createSellOrder(order);
                    }
                }
            }
        });

        response = null;
    }

    /**
     * 1. 取消订单
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void cancelOrder() {
        var client = this.apiService.getApiClient();
        var buyingList = this.orderService.getBuyingList();

        buyingList.stream().forEach(o -> {
            var orderId = o.getOrderId();
            // 撤销一小时后的未成交订单 （如果部分成交或全部成交调用撤销函数不会有错误，所以没判断是否成交，就是直接撤销所以买单）
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 1);
            date = calendar.getTime();

            if (date.getTime() - o.getBuyTime().getTime() > 0) {
                var response = client.submitcancel(orderId.toString());
                o.setStatus(OrderStatus.Done);
                this.orderService.updateOrder(o);
            }
        });
    }


}
