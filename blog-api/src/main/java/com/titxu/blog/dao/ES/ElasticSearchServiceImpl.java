package com.titxu.blog.dao.ES;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @author lxue
 * @date 2021/8/18
 * @apiNate
 */

@Slf4j
@Component
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    //@Qualifier("restHighLevelClient")
    private RestHighLevelClient client;


    /**
     * 创建索引  传入一个index索引名称 string类型的
     * 先判断索引是否存在  存在返回false
     * 索引不存在 开始创建索引  创建成功返回true
     * 否则返回false
     *
     * @param index
     * @return
     */
    @Override
    @SneakyThrows
    public boolean esCreateIndex(String index) {
        ClassPathResource classPathResource = new ClassPathResource("settings.json");
        InputStream inputStream = classPathResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(inputStream, "UTF-8"));
        inputStream.close();


        // 调用类方法  判断索引是否存在如果存在就不用创建了 返回false表示已经存在无法创建
        if (this.esExistIndex(index)) {
            return false;
        }
        CreateIndexRequest createRequest = new CreateIndexRequest(index);
        createRequest.settings(seJson, XContentType.JSON);
        //createRequest.mapping();
        CreateIndexResponse createIndexResponse = client.indices().create(createRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();

    }

    /**
     * 查询索引是否已经存在  存在true 不存在false
     *
     * @param index
     * @return
     */
    @Override
    @SneakyThrows
    public boolean esExistIndex(String index) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        return exists;

    }

    /**
     * 删除索引 成功返回true 失败返回false
     *
     * @param index
     * @return
     */
    @SneakyThrows
    @Override
    public boolean esDeleteIndex(String index) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        return delete.isAcknowledged();

    }


    /**
     * 添加文档
     *
     * @param object
     * @param id
     * @return
     */
    @SneakyThrows
    @Override
    public IndexResponse esAddDocument(Object object, String id) {
        //创建请求连接
        IndexRequest request = new IndexRequest(object.getClass().getSimpleName().toLowerCase());


        request.id(id);
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("10s"); // 超时


        //把要添加的信息放入请求中，XContentType.JSON（指定信息的类型）
        request.source(JSON.toJSONString(object), XContentType.JSON);

        //通过客户端传递求情
        IndexResponse responseResult = client.index(request, RequestOptions.DEFAULT);


        return responseResult;
    }

    /**
     * 更新文档
     *
     * @param object
     * @param id
     * @return
     */
    @Override
    @SneakyThrows
    public UpdateResponse esUpdateDocument(Object object, String id) {

        //创建修改请求，请求中指定索引，修改id
        UpdateRequest request = new UpdateRequest(object.getClass().getSimpleName().toLowerCase(), id);
        request.timeout("5s");

        //es原生api中有_doc,这里在修改的时候调用doc(),把要修改的信息放入请求中
        request.doc(JSON.toJSONString(object), XContentType.JSON);

        request.fetchSource(true);//修改之后获取结果

        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        return update;


    }

    @Override
    @SneakyThrows
    public DeleteResponse esDeltetDocument(Object object, String id) {
        DeleteRequest request = new DeleteRequest(object.getClass().getSimpleName().toLowerCase(), id);
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        return delete;
    }

    @Override
    @SneakyThrows
    public SearchHits esQueryDocument(String keyword) {
        //搜索请求
        SearchRequest searchRequest = new SearchRequest("tag");
        // 构建查询
        SearchSourceBuilder builder = new SearchSourceBuilder();


        MatchQueryBuilder simpleQueryStringBuilder = QueryBuilders.matchQuery("tagName", keyword);


        builder.query(simpleQueryStringBuilder);
        searchRequest.source(builder);


        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        log.info(String.valueOf(searchHits.getMaxScore()));

        return searchHits;

    }



    /**
     * 批量添加数据
     *
     * @param listObject
     * @return
     */
    @SneakyThrows
    public BulkResponse esBulkDocument(List<Object> listObject, String index) {
        BulkRequest request = new BulkRequest(index);
        //request.timeout("10s");
        for (int i = 0; i < listObject.size(); i++) {
            //通过批量请求，依次放入单个请求，类似入数据库的批量操作
            request.add(new IndexRequest(index)
                    .id(""+(i+1))
                    .source(JSON.toJSONString(listObject.get(i)),XContentType.JSON));
        }
        //客户端执行批量操作
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);


        return response;
    }

}
