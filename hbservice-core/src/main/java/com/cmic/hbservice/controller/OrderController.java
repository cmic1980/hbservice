package com.cmic.hbservice.controller;

import com.cmic.hbservice.domain.Order;
import com.cmic.hbservice.service.HuobiService;
import com.cmic.hbservice.service.OrderService;
import com.huobi.response.IntrustDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController("")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HuobiService huobiService;

    @RequestMapping("/order/add")
    @ResponseBody
    public HashMap add(@RequestBody Order order) {
        this.orderService.addOrder(order);
        HashMap<String,Object> result = new HashMap<>();
        result.put("result",true);
        return result;
    }

    @RequestMapping("/order/pending/list")
    @ResponseBody
    public List<Order> getPendingOrderList() {
        List<Order> orderList = this.orderService.getPendingList();
        return orderList;
    }


    @RequestMapping("/order/delete")
    @ResponseBody
    public HashMap delete(@RequestBody Order order) {
        long orderId = order.getOrderItemId();
        this.orderService.cancelOrder(orderId);
        HashMap<String,Object> result = new HashMap<>();
        result.put("result",true);
        return result;
    }

    @RequestMapping("/hb/order/list")
    @ResponseBody
    public List<IntrustDetail>  getHisOrders(){
        var result = huobiService.getOrderList("payeth");
        return result;
    }
}
