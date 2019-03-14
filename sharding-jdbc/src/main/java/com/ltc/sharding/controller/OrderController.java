package com.ltc.sharding.controller;

import com.ltc.sharding.entity.Order;
import com.ltc.sharding.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/sharding-jdbc")
    public void demo() {

    }

    @RequestMapping("/add")
    public Object add() {
        for (int i = 1; i < 21; i++) {
            Order order = new Order();
            order.setUserId((long) i);
            order.setOrderId((long) i);
            orderMapper.insert(order);
        }
        return "success";
    }

    @RequestMapping("queryAll")
    private Object queryAll() {
        return orderMapper.selectList(null);
    }


}
