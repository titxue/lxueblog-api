package com.titxu.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author lxue
 * @date 2021/7/31
 * @apiNate
 */
@Data
public class CommentVo {
    // 防止精度损失 转为字符串string
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private UserVo author;
    private String content;
    private List<CommentVo> childrens;
    private String createDate;
    private Integer level;
    private UserVo toUser;
}
