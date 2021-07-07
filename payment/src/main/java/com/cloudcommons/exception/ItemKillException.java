package com.cloudcommons.exception;

import lombok.Data;

@Data
public class ItemKillException extends RuntimeException{

    //首推
    public ItemKillException(Status status) {
        this.code=status.getCode();
        this.msg=status.getMsg();
    }

    public ItemKillException(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    private Integer code; //状态码

    private String msg; //异常信息
}
