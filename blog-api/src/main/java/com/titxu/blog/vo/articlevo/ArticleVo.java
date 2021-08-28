package com.titxu.blog.vo.articlevo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    @Id
    private Long id;

    private String title;
    private String summary;
    private Integer commentCounts;
    private Integer viewCounts;
    private Integer weight;
    private String cover;
    // 创建时间
    private String createDate;
    // 作者
    private String author;

    private List<TagVo> tags;
    private ArticleBodyVo body;
    private CategoryVo category;


}
