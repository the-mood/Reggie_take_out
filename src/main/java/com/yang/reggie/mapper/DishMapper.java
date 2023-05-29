package com.yang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yang97
 * @Date 2023/5/16 11:11
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
