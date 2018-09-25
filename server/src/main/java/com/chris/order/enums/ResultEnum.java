package com.chris.order.enums;

import lombok.Getter;

@Getter
public enum  ResultEnum {
    PARAM_ERROR(0, "参数错误"),
    CART_EMPTY(1, "购物车信息为空"),
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
