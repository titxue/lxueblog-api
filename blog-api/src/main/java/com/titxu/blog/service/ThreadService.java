package com.titxu.blog.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.titxu.blog.dao.ES.ElasticSearchService;
import com.titxu.blog.dao.mapper.ArticleESMapper;
import com.titxu.blog.dao.mapper.ArticleMapper;
import com.titxu.blog.dao.mapper.SysUserMapper;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.dao.pojo.article.Article;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThreadService {


    @Autowired
    private ElasticSearchService elasticSearchService;
    /**
     * 更新最新登录时间
     * @param sysUserMapper
     * @param sysUser
     */
    @Async("taskExecutor")
    public void updateLastLoginDate(SysUserMapper sysUserMapper, SysUser sysUser) {
        Long lastLogin = sysUser.getLastLogin();
        SysUser sysUserUpdate = new SysUser();
        sysUserUpdate.setLastLogin(System.currentTimeMillis());
        LambdaQueryWrapper<SysUser> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(SysUser::getId, sysUser.getId());
        updateWrapper.eq(SysUser::getLastLogin,lastLogin);
        sysUserMapper.update(sysUserUpdate,updateWrapper);

    }

    /**
     * 更新文章阅读总数
     * @param articleMapper
     * @param article
     */
    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {
         int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaQueryWrapper<Article> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Article::getId, article.getId());
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        articleMapper.update(articleUpdate,updateWrapper);

    }

    @Async("taskExecutor")
    public void asyncElasticSearch(ArticleESMapper articleESMapper) {
        List articles = articleESMapper.listArticleAll();
        BulkResponse bulkItemResponses = elasticSearchService.esBulkDocument(articles,"article");

    }
}
