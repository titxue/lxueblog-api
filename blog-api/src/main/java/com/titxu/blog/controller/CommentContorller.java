package com.titxu.blog.controller;


import com.titxu.blog.service.CommentService;
import com.titxu.blog.vo.params.CommentParam;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentContorller {
    @Autowired
    private CommentService commentService;

    /**
     * 获取文章评论列表
     * @param id
     * @return
     */
    @GetMapping("article/{id}")
    public Result GetComment(@PathVariable("id") Long id){
        return commentService.commentsByArticleId(id);
    }


    @PostMapping("create/change")
    public Result CreateComment(@RequestBody CommentParam commentParam){
        return commentService.commentsCreateArticle(commentParam);
    }
}
