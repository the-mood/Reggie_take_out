package com.yang.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author yang97
 * @Date 2023/5/11 10:52
 */
//配置log信息
@Slf4j
//配置为项目启动类
@SpringBootApplication
//开启WebFilter等注解扫描
@ServletComponentScan
//开启事务的支持
@EnableTransactionManagement
//开启缓存注解功能
@EnableCaching
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        //设置启动成功info日志
        log.info("项目启动成功。。。");
    }
}
