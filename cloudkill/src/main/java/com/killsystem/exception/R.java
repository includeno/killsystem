package com.killsystem.exception;

import cn.hutool.http.HttpStatus;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Data
public class R {

    private Boolean success;


    private Integer code;


    private String message;


    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private R() {}

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(HttpStatus.HTTP_OK);
        r.setMessage("成功");
        return r;
    }
    //成功静态方法
    public static R ok(String msg) {
        R r = new R();
        r.setSuccess(true);
        r.setCode(HttpStatus.HTTP_OK);
        r.setMessage(msg);
        return r;
    }

    //成功静态方法
    public static R ok(Integer code, String msg) {
        R r = new R();
        r.setSuccess(true);
        r.setCode(HttpStatus.HTTP_OK);
        r.setMessage(msg);
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(HttpStatus.HTTP_BAD_REQUEST);
        r.setMessage("失败");
        return r;
    }
    //失败静态方法
    public static R error(String msg) {
        R r = new R();
        r.setSuccess(false);
        r.setCode(HttpStatus.HTTP_BAD_REQUEST);
        r.setMessage(msg);
        return r;
    }

    //失败静态方法
    public static R error(Integer code , String msg) {
        R r = new R();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(msg);
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
