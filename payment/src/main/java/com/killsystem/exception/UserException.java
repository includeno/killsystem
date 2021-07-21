package com.killsystem.exception;

import lombok.Data;

@Data
public class UserException extends RuntimeException{

    //首推
    public UserException(Status status) {
        this.code=status.getCode();
        this.msg=status.getMsg();
    }

    public UserException(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    private Integer code; //状态码

    private String msg; //异常信息
}
