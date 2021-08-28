package com.titxu.blog.service;

import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.params.ArticleParam;
import com.titxu.blog.vo.params.PageParams;
import org.springframework.transaction.annotation.Transactional;


public interface ArticleService {

    /**
     * 分页查询 文章列表
     *
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);


    /**
     * 最热文章
     *
     * @param limit
     * @return
     */
    Result hotArticles(int limit);

    /**
     * 最新文章
     *
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档 根据年月
     *
     * @return
     */
    Result listArchives();


    /**
     * 文章内容
     *
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);


    /**
     * 创建文章
     * 接口
     *
     * @param articleParam
     * @return
     */
    @Transactional
    Result createArticle(ArticleParam articleParam);


    /**
     * 搜索接口 elasticsearch 输入关键词查询 返回搜索结果
     *
     * @param keyword
     * @return
     */
    Result searchArticles(String keyword);
}
