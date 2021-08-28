package com.titxu.blog.service;

import com.titxu.blog.vo.params.CommentParam;
import com.titxu.blog.utils.result.Result;

public interface CommentService {
    Result commentsByArticleId(Long id);

    /**
     * 添加评论
     * @param commentParam
     * @return
     */
    Result commentsCreateArticle(CommentParam commentParam);
}
