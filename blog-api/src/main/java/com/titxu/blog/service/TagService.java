package com.titxu.blog.service;

import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.articlevo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);


    /**
     * 返回所有标签
     * 接口
     * @return
     */
    Result findAll();

    /**
     * 返回所有标签详细信息
     * 接口
     * @return
     */

    Result findAllDetail();

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    Result findDetailByid(Long id);
}
