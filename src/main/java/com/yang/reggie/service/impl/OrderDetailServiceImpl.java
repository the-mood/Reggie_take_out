package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.OrderDetail;
import com.yang.reggie.entity.Orders;
import com.yang.reggie.mapper.OrderDetailMapper;
import com.yang.reggie.mapper.OrdersMapper;
import com.yang.reggie.service.OrderDetailService;
import com.yang.reggie.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/19 10:02
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
