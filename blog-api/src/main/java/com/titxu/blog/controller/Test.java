package com.titxu.blog.controller;


import com.titxu.blog.dao.ES.ElasticSearchService;
import com.titxu.blog.dao.mapper.ArticleESMapper;
import com.titxu.blog.utils.UserThreadLocal;
import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.SysUserVo;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class Test {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private ArticleESMapper articleESMapper;

    @GetMapping
    public Result test(){
        SysUserVo sysUserVo = UserThreadLocal.get();
        return Result.success(sysUserVo);
    }
    @GetMapping("{index}")
    public Result testTo(@PathVariable String index){
        boolean test = elasticSearchService.esCreateIndex(index);
        return Result.success(test);
    }
    @GetMapping("async")
    public Result async(){
        List articles = articleESMapper.listArticleAll();
        BulkResponse bulkItemResponses = elasticSearchService.esBulkDocument(articles,"article");
        return Result.success(bulkItemResponses.status());
    }

}
