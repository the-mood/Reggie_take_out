package com.yang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.reggie.common.BaseContext;
import com.yang.reggie.common.R;
import com.yang.reggie.dto.OrdersDto;
import com.yang.reggie.entity.OrderDetail;
import com.yang.reggie.entity.Orders;
import com.yang.reggie.entity.User;
import com.yang.reggie.service.OrderDetailService;
import com.yang.reggie.service.OrdersService;
import com.yang.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yang97
 * @Date 2023/5/19 10:03
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 订单支付
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){

        ordersService.submit(orders);

        return R.success("支付成功！");
    }

    /**
     * 查询用户信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getCheckoutTime);
        ordersService.page(pageInfo,queryWrapper);

        Page<OrdersDto> ordersDtoPage=new Page<>(page,pageSize);
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList=records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            LambdaQueryWrapper<OrderDetail> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,item.getId());
            wrapper.orderByDesc(OrderDetail::getAmount);
            List<OrderDetail> list = orderDetailService.list(wrapper);
            if(list!=null){
                ordersDto.setOrderDetails(list);
            }
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
}
