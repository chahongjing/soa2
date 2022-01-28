package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjy.esdemo.dto.EsResponse;
import com.zjy.esdemo.po.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 直接使用lowlevel api操作es
 */
@Component
@Slf4j
public class EsLowLevelOpt {
    @Resource
    private RestClient restClient;

    // region

    /**
     * restclient查询es
     */
    public List<Student> queryList() {
        Request request = new Request("GET", "/" + Constants.STUDENT_INDEX + "/_search");
        request.setJsonEntity(Utils.getJsonByTemplate("request3.json"));
        try {
            Response response = restClient.performRequest(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || 300 <= statusCode) {
                log.error("HTTP status code {}", response.getStatusLine().getStatusCode());
                throw new RuntimeException("HTTP status code is not 200");
            }
            String respBodyStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            EsResponse esResponse = JSON.parseObject(respBodyStr, EsResponse.class);
            JSONArray hits = esResponse.getHits().getHits();
            List<Student> list = new ArrayList<>();
            hits.forEach(item -> {
                JSONObject source = (JSONObject) ((JSONObject) item).get("_source");
                list.add(JSON.toJavaObject(source, Student.class));
            });
            return list;
        } catch (IOException e) {
            log.error("restclient request error!", e);
        }
        return null;
    }
    // endregion
}