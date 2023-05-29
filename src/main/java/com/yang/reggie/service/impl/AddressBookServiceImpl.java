package com.yang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.reggie.entity.AddressBook;
import com.yang.reggie.mapper.AddressBookMapper;
import com.yang.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/18 16:52
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
