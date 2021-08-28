package com.titxu.blog.vo.params;

import com.titxu.blog.vo.articlevo.CategoryVo;
import com.titxu.blog.vo.articlevo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author lxue
 * @date 2021/8/9
 * @apiNate
 */

@Data
public class ArticleParam {
    private Long id;
    private ArticleBodyParam body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
    private String title;

}
