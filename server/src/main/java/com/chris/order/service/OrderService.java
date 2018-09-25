package com.chris.order.service;

import com.chris.order.dto.OrderDTO;

public interface OrderService {
    OrderDTO create(OrderDTO orderDTO);
}
