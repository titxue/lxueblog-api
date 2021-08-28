package com.titxu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.blog.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 使用文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);


    /**
     * 查询最热标签前 limit 条
     * @param limit
     * @return
     */
    List<Long> findHotTagIds(int limit);

    /**
     * 根据 tagid 返回 tag对象
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
