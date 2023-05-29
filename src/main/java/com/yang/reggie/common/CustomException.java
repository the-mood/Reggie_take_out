package com.yang.reggie.common;

/**
 * 自定义业务异常
 * @Author yang97
 * @Date 2023/5/16 11:24
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
