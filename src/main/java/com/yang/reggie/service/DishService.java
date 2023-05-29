package com.yang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.reggie.dto.DishDto;
import com.yang.reggie.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/16 11:12
 */
@Service
public interface DishService extends IService<Dish> {

    //新增菜品，并保存对应的口味数据
    public void saveWithFlavor(DishDto dishDto);
    //修改带有口味数据的菜品信息
    public void updateWithFlavor(DishDto dishDto);
    //根据id查询带有口味数据的菜品信息
    public DishDto getByIdWithFlavor(Long id);


}
