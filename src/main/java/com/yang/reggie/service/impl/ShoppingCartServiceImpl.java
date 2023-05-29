package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.ShoppingCart;
import com.yang.reggie.mapper.ShoppingCartMapper;
import com.yang.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/18 19:52
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
