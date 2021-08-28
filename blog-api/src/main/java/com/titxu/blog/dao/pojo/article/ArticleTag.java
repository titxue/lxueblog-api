package com.titxu.blog.dao.pojo.article;

import lombok.Data;

/**
 * @author lxue
 * @date 2021/8/9
 * @apiNate
 */
@Data
public class ArticleTag {
    private Long id;
    private Long articleId;
    private Long tagId;
}
