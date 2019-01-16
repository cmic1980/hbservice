package com.cmic.hbservice.service;

import com.cmic.hbservice.domain.Order;
import com.huobi.api.ApiClient;
import com.huobi.response.KlineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HuobiScheduledService {
    @Value("${huobi.key.path}")
    private String accessKeyPath;

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "*/10 * * * * ?")
    public void createOrder() {


        ApiClient client = new ApiClient("","");
        KlineResponse kline = client.kline("btcusdt", "5min", "100");

        List<Order> orderList = this.orderService.getPendingList();
        orderList.forEach(o -> {

        });
    }
}
