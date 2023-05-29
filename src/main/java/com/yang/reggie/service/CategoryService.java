package com.yang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.reggie.entity.Category;
import org.springframework.stereotype.Service;

/**
 * @Author yang97
 * @Date 2023/5/16 10:32
 */
@Service
public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
