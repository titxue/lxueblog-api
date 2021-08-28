package com.titxu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.titxu.blog.dao.mapper.TagMapper;
import com.titxu.blog.dao.pojo.Tag;
import com.titxu.blog.service.TagService;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.vo.articlevo.TagVo;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public Result hots(int limit) {
        /**
         * 最热标签
         * 标签拥有最多的文章就是最热标签
         * mysql group by 进行 tag 字段分组记数 count
         * 进行降序排列 order by tag（标签） desc
         * 截取 limit 个标签
         */
        List<Long> tagIds = tagMapper.findHotTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    /**
     * 获取所以标签
     * 实现类
     * @return
     */
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    @Override
    public Result findDetailByid(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag!=null){
            return Result.success(copy(tag));
        }
        return Result.fail(CodeMsg.PARAMS_ERROR.getRetCode(),CodeMsg.PARAMS_ERROR.getMessage());
    }


    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        // mybatisplus不能多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);


        return copyList(tags);
    }

    private List<TagVo> copyList(List<Tag> tagsList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagsList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }
}
