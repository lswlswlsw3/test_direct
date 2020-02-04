package com.sicc.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sicc.order.service.OrderService;
import com.sicc.order.vo.OrderVO;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody final OrderVO orderVO) {
        final String orderId = java.util.UUID.randomUUID().toString().toUpperCase();
        orderVO.setOrderId(orderId);

        orderService.placeOrder(orderVO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
