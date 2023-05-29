package com.yang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.reggie.common.CustomException;
import com.yang.reggie.common.R;
import com.yang.reggie.dto.DishDto;
import com.yang.reggie.entity.*;
import com.yang.reggie.service.CategoryService;
import com.yang.reggie.service.DishFlavorService;
import com.yang.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yang97
 * @Date 2023/5/16 14:45
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        LambdaQueryWrapper<Dish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(Dish::getId,ids);
        queryWrapper1.eq(Dish::getStatus,1);
        //判断当前套餐是否在售卖中
        int count = dishService.count(queryWrapper1);
        if(count>0){
            throw new CustomException("当前菜品正在售卖中，请先停售后再删除！");
        }
        //根据id获取当前菜品，更改是否被删除标志
        List<Dish> dishes = dishService.listByIds(ids);
        dishes=dishes.stream().map((item)->{
            //设置菜品删除标志位
            item.setIs_deleted(1);
            //设置菜品停售
            item.setStatus(0);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishes);

        //根据id获取当前菜品的口味数据，更改是否被删除标志
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        list=list.stream().map((item)->{
            item.setIsDeleted(1);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.updateBatchById(list);
//        dishService.removeByIds(Arrays.asList(ids));
        return R.success("删除成功！");
    }

    /**
     * 停售菜品
     * @param ids
     * @return
     */
    @PostMapping({"/status/0","/status/1"})
    public R<String> updateStatus(@RequestParam List<Long> ids){
        log.info(ids.toString());
        List<Dish> dishes = dishService.listByIds(ids);

        dishes=dishes.stream().map((item)->{
            item.setStatus(item.getStatus()==0?1:0);
            return item;
        }).collect(Collectors.toList());

        dishService.updateBatchById(dishes);
        return R.success("修改状态成功！");
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品信息成功");
    }

    /**
     * 根据id查找菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getByKind(Dish dish){
        log.info("菜品分类id为{}",dish.getCategoryId());
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getPrice).orderByDesc(Dish::getUpdateTime);
        //查询分类中的菜品
        List<Dish> lists = dishService.list(queryWrapper);

        List<DishDto> dishDtoList=lists.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //将item的值复制到dishDto
            BeanUtils.copyProperties(item,dishDto);
            //获取当前菜品的类别id
            Long categoryId = item.getCategoryId();
            //根据类别id查找菜品分类
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //获取菜品分类的名称
                String categoryName = category.getName();
                //设置dishDto对象的菜品名称
                dishDto.setCategoryName(categoryName);
            }
            //获取当前菜品id
            Long id = item.getId();
            LambdaUpdateWrapper<DishFlavor> wrapper=new LambdaUpdateWrapper<>();
            wrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list = dishFlavorService.list(wrapper);
            dishDto.setFlavors(list);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> getByKind(Dish dish){
//        log.info("菜品分类id为{}",dish.getCategoryId());
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getPrice).orderByDesc(Dish::getUpdateTime);
//        //查询分类中的菜品
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构建分页构造器
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        //构建条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Dish::getIs_deleted,0);
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByAsc(Dish::getPrice);

        //执行查询
        dishService.page(pageInfo,queryWrapper);

        //问题：pageInfo的泛型为Dish，Dish中没有categoryName，前端无法显示菜品属于那类菜品
        //解决方法，新建一个分页构造器，泛型为DishDto
        Page<DishDto> dishDtoPage=new Page<>();

        //对象拷贝，将pageInfo中的数据复制到dishDtoPage中，且排除records的值，这里records的值包含所有查询到的数据
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        //获取pageInfo中records的值
        List<Dish> records = pageInfo.getRecords();
        //遍历records的值并封装到list中
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //将item的值复制到dishDto
            BeanUtils.copyProperties(item,dishDto);
            //获取当前菜品的id
            Long categoryId = item.getCategoryId();
            //根据id查找菜品
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //获取菜品的名称
                String categoryName = category.getName();
                //设置dishDto对象的菜品名称
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

}
