package com.titxu.blog.controller;

import com.titxu.blog.service.CategoryService;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxue
 * @date 2021/8/8
 * @apiNate
 */

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 返回所有分类
     *
     * @return
     */
    @GetMapping
    public Result categorys() {
        return categoryService.findAll();
    }

    /**
     * 返回所有分类详细信息
     *
     * @return
     */
    @GetMapping("detail")
    public Result categorysDetail() {
        return categoryService.findAllDetail();
    }

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id) {
        return categoryService.categoryDetailById(id);
    }
}
