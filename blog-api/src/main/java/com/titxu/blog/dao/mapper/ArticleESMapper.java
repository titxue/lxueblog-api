package com.titxu.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.titxu.blog.vo.articlevo.ArticleESVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lxue
 * @date 2021/8/19
 * @apiNate
 */
@Repository
public interface ArticleESMapper extends BaseMapper<ArticleESVo> {
    List<ArticleESVo> listArticleAll();
}
