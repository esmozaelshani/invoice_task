package com.example.task.service;


import com.example.task.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper){
        this.orderMapper = orderMapper;
    }

}
