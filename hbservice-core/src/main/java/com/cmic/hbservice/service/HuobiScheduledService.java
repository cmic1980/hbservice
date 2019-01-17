package com.cmic.hbservice.service;

import com.alibaba.fastjson.JSONObject;
import com.cmic.hbservice.domain.Order;

import com.cmic.hbservice.domain.OrderStatus;
import com.huobi.api.ApiClient;
import com.huobi.request.CreateOrderRequest;
import com.huobi.response.Accounts;
import com.huobi.response.AccountsResponse;
import com.huobi.response.Kline;
import com.huobi.response.KlineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
        if(request.price.length()>8)
        {
            request.price = request.price.substring(0,8);
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

    /**
     *  1. 检查挂单成交状态
     */
    // @Scheduled(cron = "*/5 * * * * ?")
    public void sellOrder(){
        var client = this.apiService.getApiClient();

        var response = client.balance(this.getAccountId().toString());


        /*
        var buyingList = this.orderService.getBuyingList();
        buyingList.stream().forEach(o->{
            var orderId = o.getOrderId();
            var response = client.ordersDetail(orderId.toString());
            response = null;
        });*/

        // client.ordersDetail()
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void cancelOrder(){
        var client = this.apiService.getApiClient();
        var buyingList = this.orderService.getBuyingList();
        buyingList.stream().forEach(o->{
            var orderId = o.getOrderId();
            var response = client.submitcancel(orderId.toString());
            o.setStatus(OrderStatus.Done);
            this.orderService.updateOrder(o);
        });

    }
}
