package com.zjy.esdemo.service.impl;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 非starter配置方式
 */
@Configuration
public class EsEnvProperties {
    @Value("${es.protocol}")
    private String protocol;
    @Value("${es.host}")
    private String host;
    @Value("${es.port}")
    private int port;
    @Value("${es.username}")
    private String username;
    @Value("${es.password}")
    private String password;

    /**
     * 不带密码验证
     * @return
     */
    @Bean("restHighLevelClientPre")
    public RestHighLevelClient getRestHighLevelClient() {
//        Header[] defaultHeaders = new Header[]{new BasicHeader("Authorization", "Basic abcdefefsfasdfafef==")};
//        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)).setDefaultHeaders(defaultHeaders));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)));
    }

    /**
     * 带密码验证
     * @return
     */
//    @Bean
    public RestHighLevelClient restHighLevelClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)).
                setHttpClientConfigCallback((HttpAsyncClientBuilder httpAsyncClientBuilder) ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));
    }

    /**
     * 带密码验证
     * @return
     */
//    @Bean
    public RestHighLevelClient restHighLevelClient2() {
        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Authorization", "Basic eGlhb21pOnhpYW9taQ=="));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)).
                setHttpClientConfigCallback((HttpAsyncClientBuilder httpAsyncClientBuilder) ->
                        httpAsyncClientBuilder.setDefaultHeaders(defaultHeaders)));
    }
}
