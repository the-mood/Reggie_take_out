package com.yang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yang97
 * @Date 2023/5/18 15:28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
