package com.yang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yang97
 * @Date 2023/5/11 11:23
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
