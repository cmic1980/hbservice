package com.cmic.hbservice.service.impl;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.domain.OrderStatus;
import com.cmic.hbservice.service.ApiService;
import com.cmic.hbservice.service.HuobiService;
import com.huobi.request.CreateOrderRequest;
import com.huobi.request.IntrustOrdersDetailRequest;
import com.huobi.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class HuobiServiceImpl implements HuobiService {
    @Autowired
    private ApiService apiService;
    private Integer accountId;
    private List<Symbol> symbolList;

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
    public BigDecimal getBuyPrice(String symbol) {
        var client = this.apiService.getApiClient();
        KlineResponse response = client.kline(symbol, "60min", "1");

        var klineList = (ArrayList<Kline>) response.data;
        var kline = klineList.get(0);

        BigDecimal buyPrice = new BigDecimal(kline.getOpen());
        return buyPrice;
    }

    public long createBuyOrder(Order order) {
        var client = this.apiService.getApiClient();

        if (this.symbolList == null) {
            this.symbolList = client.getSymbols();
        }

        // create order:
        CreateOrderRequest request = new CreateOrderRequest();
        request.accountId = String.valueOf(this.getAccountId());
        request.amount = order.getAmount().toString();

        BigDecimal price = order.getBuyPrice();
        request.price = price.toString();
        request.symbol = order.getSymbol();

        // 根据币种别名获取币种，确定币种价格精度
        var currency = this.getCurrency(request.symbol);
        int scale = Integer.valueOf(currency.pricePrecision);

        // 0.00001874 小数点都8位，包括小数点和0一共10位
        if (request.price.length() > scale + 2) {
            request.price = request.price.substring(0, scale + 2);
        }
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
        Double amount = Math.floor(order.getAmount().doubleValue());

        if (amount < 0.05) {
            return -1;
        }

        request.amount = amount.toString();

        BigDecimal price = order.getSellPrice();
        request.price = price.toString();


        // 根据币种别名获取币种，确定币种价格精度
        var currency = this.getCurrency(order.getSymbol());
        int scale = Integer.valueOf(currency.pricePrecision);

        // 0.00001874 小数点都8位，包括小数点和0一共10位
        if (request.price.length() > scale + 2) {
            request.price = request.price.substring(0, scale + 2);
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

    @Override
    public List<HashMap> getBalanceItemList() {
        var client = this.apiService.getApiClient();
        var response = client.balance(this.getAccountId().toString());
        var balance = (Balance) response.getData();
        var itemList = (List<HashMap>) balance.getList();
        return itemList;
    }

    @Override
    public List<IntrustDetail> getOrderList(String symbol) {
        var client = this.apiService.getApiClient();

        IntrustOrdersDetailRequest req = new IntrustOrdersDetailRequest();
        req.symbol = symbol;
        req.types = IntrustOrdersDetailRequest.OrderType.BUY_LIMIT;
        req.states = IntrustOrdersDetailRequest.OrderStates.FILLED;
        req.startDate = "2019-01-18";
        req.endDate = "2019-01-19";
        IntrustDetailResponse response = client.intrustOrdersDetail(req);

        List<IntrustDetail> huobiOrderList = (List<IntrustDetail>) response.getData();
        return huobiOrderList;
    }

    private Symbol getCurrency(String symbol) {
        var client = this.apiService.getApiClient();

        if (this.symbolList == null) {
            this.symbolList = client.getSymbols();
        }

        Symbol currency = this.symbolList.stream().filter(s -> s.symbol.equals(symbol)).findAny().get();
        return currency;
    }
}
