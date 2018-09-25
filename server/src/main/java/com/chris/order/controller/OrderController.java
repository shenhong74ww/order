package com.chris.order.controller;

import com.chris.order.converter.OrderForm2OrderDTOConverter;
import com.chris.order.dto.OrderDTO;
import com.chris.order.enums.ResultEnum;
import com.chris.order.exception.OrderException;
import com.chris.order.form.OrderForm;
import com.chris.order.service.OrderService;
import com.chris.order.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityListeners;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResultVo<Map<String, String>> create(@RequestBody OrderForm orderForm) {
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车信息为空");
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }

        OrderDTO result = orderService.create(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());
        return new ResultVo(map);
    }

}
