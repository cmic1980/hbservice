package com.cmic.hbservice.controller;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.service.AnalysisResultService;
import com.cmic.hbservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController("")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/add")
    @ResponseBody
    public HashMap add(@RequestBody Order order) {
        this.orderService.addOrder(order);
        HashMap<String,Object> result = new HashMap<>();
        result.put("result",true);
        return result;
    }
}
