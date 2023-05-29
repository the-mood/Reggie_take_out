package com.yang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.reggie.dto.OrdersDto;
import com.yang.reggie.entity.Orders;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/19 10:01
 */
@Service
public interface OrdersService extends IService<Orders> {
    //下单
    public void submit(Orders orders);

}
