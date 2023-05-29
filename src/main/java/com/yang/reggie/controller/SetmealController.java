package com.yang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.reggie.common.CustomException;
import com.yang.reggie.common.R;
import com.yang.reggie.dto.SetmealDto;
import com.yang.reggie.entity.Category;
import com.yang.reggie.entity.Dish;
import com.yang.reggie.entity.Setmeal;
import com.yang.reggie.entity.SetmealDish;
import com.yang.reggie.service.CategoryService;
import com.yang.reggie.service.SetmealDishService;
import com.yang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yang97
 * @Date 2023/5/17 10:32
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.saveWithDishs(setmealDto);

        return R.success("套餐增加成功!");
    }

    /**
     * 根据id删除套餐
     * @param ids
     * @return
     */
    @Transactional
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(Setmeal::getId,ids);
        queryWrapper1.eq(Setmeal::getStatus,1);
        //判断当前套餐是否在售卖中
        int count = setmealService.count(queryWrapper1);
        if(count>0){
            throw new CustomException("当前套餐正在售卖中，请先停售后再删除！");
        }

        //更新套餐表setmeal表信息
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        setmeals=setmeals.stream().map((item)->{
            //设置套餐停售
            item.setStatus(0);
            //设置套餐删除标志位
            item.setIsDeleted(1);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(setmeals);

        //更新setmeal_dish表信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        list=list.stream().map((item)->{
            item.setIsDeleted(1);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.updateBatchById(list);

        return R.success("删除套餐成功！");
    }

    /**
     * 启售/停售套餐
     * @param ids
     * @return
     */
    @PostMapping({"/status/0","/status/1"})
    public R<String> updateStatus(@RequestParam List<Long> ids){
        List<Setmeal> setmeals = setmealService.listByIds(ids);

        setmeals=setmeals.stream().map((item)->{
            item.setStatus(item.getStatus()==0?1:0);
            return item;
        }).collect(Collectors.toList());

        setmealService.updateBatchById(setmeals);

        return R.success("修改套餐状态成功");
    }

    /**
     * 修改带有菜品信息的套餐数据
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){

        setmealService.updateWithDishs(setmealDto);

        return R.success("修改套餐信息成功");
    }

    /**
     * 根据id查找带有菜品信息的套餐数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        log.info("要修改的套餐id为{}",id);

        SetmealDto setmealDto = setmealService.getByIdWithDishs(id);

        return R.success(setmealDto);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构建分页构造器
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        //构建条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Setmeal::getIsDeleted,0);
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        queryWrapper.orderByAsc(Setmeal::getPrice);

        //执行查询
        setmealService.page(pageInfo,queryWrapper);

        //新建分页构造器
        Page<SetmealDto> setmealDtoPage=new Page<>(page,pageSize);
        //将其他普通信息拷贝到新的分页构造器，并排除records数据
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //获取套餐分类名称
                String setmealName = category.getName();
                setmealDto.setCategoryName(setmealName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 根据条件查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByAsc(Setmeal::getPrice);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
