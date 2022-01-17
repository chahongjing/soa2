package com.zjy.esdemo.service.impl;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .connectedTo("10.138.130.1:30217","10.138.130.1:32002","10.138.130.1:33785")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
//    @Bean(name = { "elasticsearchOperations", "elasticsearchRestTemplate" })
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
