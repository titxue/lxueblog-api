package com.titxu.blog.vo.articlevo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryVo {
    private Long id;
    private String avatar;
    private String categoryName;
    private String description;
}
