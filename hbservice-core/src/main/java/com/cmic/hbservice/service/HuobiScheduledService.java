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
                buyPrice = buyPrice / 2;
                o.setBuyPrice(buyPrice);
                this.createBuyOrder(o);
            }

            /*
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            // create order:
            CreateOrderRequest createOrderReq = new CreateOrderRequest();
            createOrderReq.accountId = String.valueOf(this.getAccountId());
            createOrderReq.amount = o.getAmount().toString();
            createOrderReq.price = o.getBuyPrice().toString();
            createOrderReq.symbol = o.getSymbol();
            createOrderReq.type = CreateOrderRequest.OrderType.BUY_LIMIT;
            createOrderReq.source = "api";
            */

            //------------------------------------------------------ 创建订单  -------------------------------------------------------
            // long orderId = client.createOrder(createOrderReq);

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
}
