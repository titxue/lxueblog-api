package com.titxu.blog.service;

import com.titxu.blog.vo.articlevo.CategoryVo;
import com.titxu.blog.utils.result.Result;


/**
 * 根据category id 查询category 分类
 */
public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);


    /**
     * 返回所有分类
     * 接口
     * @return
     */
    Result findAll();
    /**
     * 返回所有分类详细信息
     * 接口
     * @return
     */
    Result findAllDetail();


    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Result categoryDetailById(Long id);
}
