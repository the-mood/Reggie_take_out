package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.DishFlavor;
import com.yang.reggie.mapper.DishFlavorMapper;
import com.yang.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/16 15:30
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
