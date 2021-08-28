package com.titxu.blog.dao.ES;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.search.SearchHits;

import java.util.List;

/**
 * @author lxue
 * @date 2021/8/18
 * @apiNate
 */


public interface ElasticSearchService {

    //esCreateIndex()

    /**
     * 创建索引
     * @param index
     * @return
     */
    boolean esCreateIndex(String index);

    /**
     * 查看索引是否存在
     * @param index
     * @return
     */
    boolean esExistIndex(String index);

    /**
     * 删除索引
     * @param index
     * @return
     */
    boolean esDeleteIndex(String index);

    /**
     * 创建文档
     * @param object
     * @param id
     * @return
     */
    IndexResponse esAddDocument(Object object, String id);

    /**
     * 更新文档
     * @param object
     * @return
     */
    UpdateResponse esUpdateDocument(Object object, String id);

    /**
     * 删除文档
     * @param object
     * @return
     */
    DeleteResponse esDeltetDocument(Object object, String id);


    /**
     * 查询文档
     * @param keyword
     * @return
     */
    SearchHits esQueryDocument(String keyword);


    /**
     * 批量添加文档
     * @param listObject
     * @return
     */
    BulkResponse esBulkDocument(List<Object> listObject,String index);







}
