package com.chris.order.converter;

import com.chris.order.dataobject.OrderDetail;
import com.chris.order.dto.CartDTO;
import com.chris.order.dto.OrderDTO;
import com.chris.order.form.OrderForm;

import java.util.ArrayList;
import java.util.List;

public class OrderForm2OrderDTOConverter {
    public static OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerOpenid(orderForm.getOpenId());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartDTO cartDTO : orderForm.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(cartDTO.getProductId());
            orderDetail.setProductQuantity(cartDTO.getProductQuantity());
            orderDetailList.add(orderDetail);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
