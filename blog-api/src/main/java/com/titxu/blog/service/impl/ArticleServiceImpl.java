package com.titxu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.titxu.blog.dao.dos.Archives;
import com.titxu.blog.dao.mapper.ArticleBodyMapper;
import com.titxu.blog.dao.mapper.ArticleMapper;
import com.titxu.blog.dao.mapper.ArticleTagMapper;
import com.titxu.blog.dao.pojo.article.Article;
import com.titxu.blog.dao.pojo.article.ArticleBody;
import com.titxu.blog.dao.pojo.article.ArticleTag;
import com.titxu.blog.service.*;
import com.titxu.blog.utils.UserThreadLocal;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.SysUserVo;
import com.titxu.blog.vo.articlevo.ArticleBodyVo;
import com.titxu.blog.vo.articlevo.ArticleVo;
import com.titxu.blog.vo.articlevo.TagVo;
import com.titxu.blog.vo.params.ArticleParam;
import com.titxu.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    //@Autowired
    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    // 重复利用
    private LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
    /**
     * 返回文章列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         *  分页查询数据库表 article 返回结果
         */
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth()
        );
        System.out.println("----------------------");
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true,false,true));

    }

    /**
     * 返回文章列表
     *
     * @param pageParams
     * @return
     */
    //@Override
    public Result listArticleAbandon(PageParams pageParams) {
        /**
         *  分页查询数据库表 article 返回结果
         */
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (pageParams.getCategoryId()!=null){
            // 查询条件  categoryid不等于空   and category_id=#{categoryId}
            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
        }
        List<Long> articleIdList = new ArrayList<>();
        if (pageParams.getTagId()!=null){
            // 查询条件 根据id列表筛选文章
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
            for (ArticleTag articleTag : articleTags) {
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList.size()>0){
                queryWrapper.in(Article::getId,articleIdList);
            }

        }
        // 1.置顶排序 2.时间排序
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(records, true, true, false, true);
        return Result.success(articleVoList);
    }

    /**
     * 最热文章
     *
     * @param limit
     * @return
     */
    @Override
    public Result hotArticles(int limit) {
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // SELECT * FROM tit_article ORDER BY view_counts DESC LIMIT 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false, false, false));
    }

    /**
     * 最新文章 根据时间排序 create_data
     *
     * @param limit
     * @return
     */
    @Override
    public Result newArticles(int limit) {
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        // SELECT * FROM tit_article ORDER BY create_date DESC LIMIT 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }


    /**
     * 文章归档
     *
     * @return
     */
    @Override
    public Result listArchives() {
        // SELECT year(FROM_UNIXTIME(create_date/1000,'%Y-%m-%d %H:%i:%s')) as year,
        // month(FROM_UNIXTIME(create_date/1000,'%Y-%m-%d %H:%i:%s')) as month,
        // count(*) as count from tit_article GROUP BY year,month
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }


    /**
     * 根据文章id  articleid 查询文章内容
     *
     * @param articleId
     * @return
     */
    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 根据id 查询文章信息
         * 根据bodyid做category关联查询
         */
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        if (articleVo == null) {
            return Result.fail(CodeMsg.ARTICLE_BODY_NOT_EXIST.getRetCode(), CodeMsg.ARTICLE_BODY_NOT_EXIST.getMessage());
        }
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }


    /**
     * 创建文章实现类
     * 1.获取登录用户  再threadlocal获取用户对象
     * 2.构建article文章对象 创建文章
     * 3.标签 添加到关联表中
     * 4.内容添加到文章 article body
     *
     * @param articleParam
     * @return
     */
    @Override
    public Result createArticle(ArticleParam articleParam) {
        SysUserVo sysUserVo = UserThreadLocal.get();
        if (sysUserVo==null){
            Result.fail(CodeMsg.LOGIN_NOT_CREATE_ARTICLE_ERROR.getRetCode(), CodeMsg.LOGIN_NOT_CREATE_ARTICLE_ERROR.getMessage());
        }

        Article article = new Article();
        article.setAuthorId(sysUserVo.getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setCategoryId(articleParam.getCategory().getId());
        this.articleMapper.insert(article);
        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        HashMap<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);

    }

    @Override
    public Result searchArticles(String keyword) {
        return null;
    }

    /**
     * 列表对象复制
     *
     * @param records
     * @param isTag
     * @param isAuthor
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    /**
     * 复制单个对象
     *
     * @param article
     * @param isTag
     * @param isAuthor
     * @return
     */
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        /**
         * 返回前端数据  copy
         */
        ArticleVo articleVo = new ArticleVo();
        if (article == null) {
            return null;
        }
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        // 判断是否需要tag标签 和 作者信息author
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
//        if (articleBody==null){
//            Result.fail(CodeMsg.ARTICLE_BODY_NOT_EXIST.getRetCode(), CodeMsg.ARTICLE_BODY_NOT_EXIST.getMessage());
//        }
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setId(articleBody.getId());
        articleBodyVo.setContent(articleBody.getContent());
        articleBodyVo.setContentHtml(articleBody.getContentHtml());
        return articleBodyVo;
    }
}
