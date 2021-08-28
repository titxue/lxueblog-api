package com.titxu.blog.vo.params;

import lombok.Data;

/**
 * @author lxue
 * @date 2021/8/7
 * @apiNate
 */
@Data
public class CommentParam {
    private Long articleId;
    private String content;
    private Long parent;
    private Long toUserId;
}
