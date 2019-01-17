package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.cmic.hbservice.service.ApiService;
import com.cmic.hbservice.service.HuobiService;
import com.cmic.hbservice.service.OrderService;
import com.huobi.request.CreateOrderRequest;
import com.huobi.response.Accounts;
import com.huobi.response.AccountsResponse;
import com.huobi.response.Kline;
import com.huobi.response.KlineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HuobiServiceImpl implements HuobiService {
    @Autowired
    private ApiService apiService;
    private Integer accountId;

    public Integer getAccountId() {
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
    public Double getBuyPrice(String symbol) {
        var client = this.apiService.getApiClient();
        KlineResponse response = client.kline(symbol, "60min", "1");

        var klineList = (ArrayList<Kline>) response.data;
        var kline = klineList.get(0);
        var buyPrice = kline.getOpen();
        return buyPrice;
    }

    public long createBuyOrder(Order order) {
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

        return orderId;
    }

    public long createSellOrder(Order order) {
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
        return orderId;
    }

    @Override
    public void cancelOrder(Long orderId) {
        var client = this.apiService.getApiClient();
        client.submitcancel(orderId.toString());
    }
}
