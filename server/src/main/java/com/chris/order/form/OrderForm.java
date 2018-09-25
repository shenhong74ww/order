package com.chris.order.form;

import com.chris.order.dto.CartDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderForm {
    private String name;
    private String phone;
    private String address;
    private String openId;
    private List<CartDTO> items;
}
