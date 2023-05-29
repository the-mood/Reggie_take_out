package com.yang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author yang97
 * @Date 2023/5/18 16:51
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
