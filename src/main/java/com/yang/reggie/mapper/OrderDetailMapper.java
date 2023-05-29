package com.yang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.reggie.entity.OrderDetail;
import com.yang.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yang97
 * @Date 2023/5/19 10:00
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
