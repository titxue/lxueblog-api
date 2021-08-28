package com.titxu.blog;

import com.titxu.blog.dao.ES.ElasticSearchService;
import com.titxu.blog.dao.mapper.ArticleESMapper;
import com.titxu.blog.dao.pojo.Tag;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lxue
 * @date 2021/8/16
 * @apiNate
 */
@SpringBootTest
//@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private ArticleESMapper articleESMapper;




    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
     void testCreatIndex()  {

        boolean test = elasticSearchService.esCreateIndex("test99");
        System.out.println(test);


    }

    /**
     * 查找索引 返回是否存在
     *
     * @throws IOException
     */
    @Test
    public void testExistIndex() throws IOException {
        boolean exists = elasticSearchService.esExistIndex("test");
        System.out.println(exists);


    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    void testDeleteIndex() throws IOException {
        Boolean delete = elasticSearchService.esDeleteIndex("test11");
        System.out.println(delete);

    }

    /**
     * 创建文档
     *
     * @throws IOException
     */

    @Test
    public void testAddDocument() {
        Tag tag = new Tag();
        tag.setTagName("python springjava");
        tag.setAvatar("nas.titxu.com");
        tag.setId(4L);

        Object test11 = elasticSearchService.esAddDocument(tag,tag.getId().toString());
        System.out.println(test11);


    }



    @Test
    void testUpdateDocument(){
        Tag tag = new Tag();
        tag.setTagName("java spring");
        tag.setAvatar("nas.titxu.com");
        tag.setId(1L);
        Object o = elasticSearchService.esUpdateDocument(tag, "1");
        System.out.println(o);
    }


    @Test
    void testQueryDocument(){
        SearchHits j = elasticSearchService.esQueryDocument("springjava");
        System.out.println(j);
    }

    @Test
    void  queryDocumentTest(){
        List list = new ArrayList<>();
        for (int i = 1; i < 100+1; i++) {
            Tag tag = new Tag();
            tag.setId((long) i);
            tag.setTagName("tses"+i);
            tag.setAvatar("avatar"+i);
            list.add(tag);
        }
        BulkResponse tag = elasticSearchService.esBulkDocument(list, "tag");
        System.out.println(tag.status());

    }

    @Test
    void asyncTest(){
        List articles = articleESMapper.listArticleAll();
        BulkResponse bulkItemResponses = elasticSearchService.esBulkDocument(articles,"article");
    }





}
