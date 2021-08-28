package com.titxu.blog.vo.articlevo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagVo {
    private Long id;
    private String tagName;
    private String avatar;
}
