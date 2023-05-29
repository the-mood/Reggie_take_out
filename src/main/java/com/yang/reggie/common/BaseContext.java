package com.yang.reggie.common;

/**
 * 基于ThreadLocal封装工具类，新增和更新用户时获取当前用户登录id
 * @Author yang97
 * @Date 2023/5/16 10:16
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
