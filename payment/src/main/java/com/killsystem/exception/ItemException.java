package com.killsystem.exception;

import lombok.Data;

@Data
public class ItemException extends RuntimeException{

    //首推
    public ItemException(Status status) {
        this.code=status.getCode();
        this.msg=status.getMsg();
    }

    public ItemException(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    private Integer code; //状态码

    private String msg; //异常信息
}
