package com.titxu.blog.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lxue
 * @date 2021/8/16
 * @apiNate
 */
@Configuration
public class ElasticsearchClientConfig {
    /**
     * ES地址,ip:port
     */
    @Value("${elasticsearch.ip}")
    String ipPort;

    @Value("${elasticsearch.userName}")
    String userName;
    @Value("${elasticsearch.Password}")
    String password;

    @Bean
    public RestHighLevelClient elasticsearchRestTemplate() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 构建链接用户授权对象
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(makeHttpHost(ipPort));


        // 异步连接数配置
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });


        return new RestHighLevelClient(builder);
    }


    // 转换成 HttpHost
    private HttpHost makeHttpHost(String s) {
        String[] address = s.split(":");
        String ip = address[0];
        int port = Integer.parseInt(address[1]);

        return new HttpHost(ip, port, "http");
    }
}