package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.dto.SetmealDto;
import com.yang.reggie.entity.Setmeal;
import com.yang.reggie.entity.SetmealDish;
import com.yang.reggie.mapper.SetmealMapper;
import com.yang.reggie.service.SetmealDishService;
import com.yang.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yang97
 * @Date 2023/5/16 11:14
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，并保存对应菜品
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDishs(SetmealDto setmealDto) {
        //保存套餐的基本信息到setmeal表中
        this.save(setmealDto);
        //获取套餐id
        Long setmealId = setmealDto.getId();
        //获取套餐中包含的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());

        //保存当前套餐菜品到setmeal—dish表中
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 修改带有菜品信息的套餐数据
     * @param setmealDto
     */
    @Transactional
    @Override
    public void updateWithDishs(SetmealDto setmealDto) {
        //更新setmeal表信息
        this.updateById(setmealDto);
        //清除当前菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //添加新的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);


    }

    /**
     * 根据id查找包含菜品信息的套餐数据
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDishs(Long id) {
        SetmealDto setmealDto=new SetmealDto();

        Setmeal setmeal = this.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }


}
