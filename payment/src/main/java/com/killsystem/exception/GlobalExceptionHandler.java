package com.killsystem.exception;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : includeno
 * @date : 2021
 */
@ControllerAdvice

public class GlobalExceptionHandler {
    public Logger log= LoggerFactory.getLogger("GlobalExceptionHandler");

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R error(CustomException e){
        log.error(e.getMsg());
        return R.error().code(e.getCode()).message(e.getMsg());
    }

    @ExceptionHandler(ItemException.class)
    @ResponseBody
    public R error(ItemException e){
        log.error(e.getMsg());
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
