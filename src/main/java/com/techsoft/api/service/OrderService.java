package com.techsoft.api.service;

import com.techsoft.api.common.AbstractService;
import com.techsoft.api.domain.Order;
import com.techsoft.api.dto.OrderDto;
import com.techsoft.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends AbstractService<Order, OrderDto> {

    @Autowired
    OrderService(OrderRepository orderRepository) {
        super(orderRepository, Order.class);
    }
}
