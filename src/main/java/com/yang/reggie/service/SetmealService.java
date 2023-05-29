package com.yang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.reggie.dto.SetmealDto;
import com.yang.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/16 11:12
 */
@Service
public interface SetmealService extends IService<Setmeal> {
    //新增套餐，并保存对应菜品
    public void saveWithDishs(SetmealDto setmealDto);

    //修改带有菜品信息的套餐数据
    public void updateWithDishs(SetmealDto setmealDto);

    //根据id查找包含菜品信息的套餐数据
    public SetmealDto getByIdWithDishs(Long id);
}
