package com.chris.order.vo;

import lombok.Data;

@Data
public class ResultVo<T> {
    private Integer code;
    private String msg;
    private T data;

    public ResultVo(T data) {
        this.code = 200;
        this.msg = "成功";
        this.data = data;
    }
}
