package com.titxu.blog.vo.articlevo;

import lombok.Data;

/**
 * @author lxue
 * @date 2021/8/19
 * @apiNate
 */
@Data
public class ArticleESVo {
    private Long id;
    private String title;
    private String summary;
    private String content;
}
