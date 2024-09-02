package com.zjy.esdemo.service.impl;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.reactor.IOReactorException;
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
    @Bean
    public RestHighLevelClient restHighLevelClientBuilderWithNoAuth() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)));
    }

    /**
     * header带密码验证
     * @return
     */
//    @Bean
    public RestHighLevelClient restHighLevelClientBuilderWithHeaderAuth() {
        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Authorization", "Basic abcdefefsfasdfafef=="));
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol)).
                setHttpClientConfigCallback((HttpAsyncClientBuilder builder) -> builder.setDefaultHeaders(defaultHeaders)));
    }

    /**
     * 带密码验证
     * 如果想使用spring自动装配，可以实现RestClientBuilderCustomizer接口，配置RestClientBuilder的信息
     * @return
     */
//    @Bean
    public RestHighLevelClient restHighLevelClientBuilderWithPasswordAuth() throws IOReactorException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000)
                .setConnectionRequestTimeout(3000).build();
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(
                new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
        cm.setMaxTotal(50);
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, protocol))
                .setHttpClientConfigCallback((HttpAsyncClientBuilder builder) ->
                        builder.setDefaultCredentialsProvider(credentialsProvider)
                                .setDefaultRequestConfig(requestConfig)
                                .setMaxConnTotal(10).setMaxConnPerRoute(10)
//                                .setDefaultIOReactorConfig(
//                                        IOReactorConfig.custom()
//                                                .setIoThreadCount(10) // 设置线程数
//                                                .build()).setConnectionManager(cm)
                )
        );
    }

    /**
     * restClient
     * @return
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost(host, port, protocol)).build();
//        return restHighLevelClient.getLowLevelClient();
    }
}
