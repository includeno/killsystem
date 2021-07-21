package com.killsystem.exception;

public enum Status {

    ITEM_NAME_EXIST(1,"商品名称已存在"),
    ITEM_NOT_ALLOWED(2,"商品不符合条件"),


    ITEMKILL_NOT_EXIST(3,"秒杀商品不存在"),
    ITEMKILL_NOT_ALLOWED(4,"秒杀商品不符合条件"),

    USER_NAME_EXIST(1,"用户名称已存在"),
    USER_NOT_ALLOWED(2,"用户不符合条件"),
    ;

    Integer code;
    String msg;

    Status(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
