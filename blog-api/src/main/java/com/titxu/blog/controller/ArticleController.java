package com.titxu.blog.controller;


import com.titxu.blog.common.aop.Cache.CacheAnnotation;
import com.titxu.blog.common.aop.Log.LogAnnotation;
import com.titxu.blog.service.ArticleService;
import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.params.ArticleParam;
import com.titxu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// json数据
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    /**
     * 创建文章
     * @param articleParam
     * @return
     */
    @PostMapping("create")
    public Result create(@RequestBody ArticleParam articleParam) {
        return articleService.createArticle(articleParam);

    }

    /**
     * 首页 文章列表
     *
     * @param pageParams
     * @return
     */
    @PostMapping
    @CacheAnnotation(name = "listArticle", expires = 5 * 60 * 1000)
    @LogAnnotation(module = "文章", operator = "文章列表") //记录日志注解
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);

    }

    /**
     * 最热文章 根据 ViewCounts 排序
     *
     * @return
     */

    @PostMapping("hot")
    @CacheAnnotation(name = "hotArticle", expires = 5 * 60 * 1000)
    @LogAnnotation(module = "文章", operator = "最热文章") //记录日志注解
    public Result hotArticles() {
        int limit = 5;
        return articleService.hotArticles(limit);

    }

    /**
     * 最新文章 根据 create_date 排序
     *
     * @return
     */

    @PostMapping("new")
    @CacheAnnotation(name = "newArticle", expires = 5 * 60 * 1000)
    @LogAnnotation(module = "文章", operator = "最新文章") //记录日志注解
    public Result newArticles() {
        int limit = 5;
        return articleService.newArticles(limit);

    }

    /**
     * 文章归档
     *
     * @return
     */

    @PostMapping("archive")
    @LogAnnotation(module = "文章", operator = "文章归档") //记录日志注解
    @CacheAnnotation(name = "archiveArticle", expires = 5 * 60 * 1000)
    public Result listArchives() {
        return articleService.listArchives();

    }

    /**
     * 获取文章内容
     * 根据文章id
     *
     * @param articleId
     * @return
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 搜索文章  采用elasticsearch为搜索引擎
     * 根据文章id
     * @param keyword
     * @return
     */
    @GetMapping("search/{keyword}")
    public Result searchArticles(@PathVariable("keyword") String keyword) {
        return articleService.searchArticles(keyword);
    }
}
