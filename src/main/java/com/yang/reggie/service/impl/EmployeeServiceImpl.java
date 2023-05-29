package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.Employee;
import com.yang.reggie.mapper.EmployeeMapper;
import com.yang.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/11 11:25
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
