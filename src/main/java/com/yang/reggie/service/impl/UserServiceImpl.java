package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.User;
import com.yang.reggie.mapper.UserMapper;
import com.yang.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/18 15:29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
