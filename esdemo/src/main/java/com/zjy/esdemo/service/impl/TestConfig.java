package com.zjy.esdemo.service.impl;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

//@Configuration
public class TestConfig extends AbstractElasticsearchConfiguration {
//    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
//                .withBasicAuth(name, password)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
//    @Bean(name = { "elasticsearchOperations", "elasticsearchRestTemplate" })
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
