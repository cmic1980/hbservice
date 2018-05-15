package com.cmic.hbservice.controller;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.service.AnalysisResultService;
import com.cmic.hbservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/add")
    public void add(@RequestBody Order order) {
        this.orderService.addOrder(order);
    }
}
