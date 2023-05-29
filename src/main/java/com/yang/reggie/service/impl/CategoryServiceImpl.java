package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.common.CustomException;
import com.yang.reggie.entity.Category;
import com.yang.reggie.entity.Dish;
import com.yang.reggie.entity.Setmeal;
import com.yang.reggie.mapper.CategoryMapper;
import com.yang.reggie.service.CategoryService;
import com.yang.reggie.service.DishService;
import com.yang.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/16 10:32
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count=dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果关联，抛出一个业务异常
        if(count>0){
            //已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类下关联菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealServiceLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealServiceLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2=setmealService.count(setmealServiceLambdaQueryWrapper);
        if(count2>0){
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类下关联套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
