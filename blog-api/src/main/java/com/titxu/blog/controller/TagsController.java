package com.titxu.blog.controller;


import com.titxu.blog.service.TagService;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {


    @Autowired
    private TagService tagService;

    /**
     * 返回所有标签
     * @return
     */
    @GetMapping
    public Result tags(){
        return tagService.findAll();
    }

    /**
     * 返回所有标签详细信息
     * @return
     */
    @GetMapping("detail")
    public Result detail(){
        return tagService.findAllDetail();
    }


    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public Result findDetailByid(@PathVariable("id") Long id){
        return tagService.findDetailByid(id);
    }

    /**
     * 返回最热标签
     * @return
     */
    @GetMapping("hot")
    public Result hot(){
        int limit = 5;
        return tagService.hots(limit);
    }





}
