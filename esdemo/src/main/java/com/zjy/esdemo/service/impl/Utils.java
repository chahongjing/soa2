package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSONReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;

@Slf4j
public class Utils {
    public static String getJsonReader(String srcPath) {
        try {
            String resource = Utils.class.getClassLoader().getResource("").toString();
            if(resource.startsWith("file:/")) {
                resource = resource.substring(5);
            }
            FileReader fileReader=new FileReader(resource + srcPath);
            return new JSONReader(fileReader).readString();
        } catch (Exception e) {
            log.error("loadTestData: 读取失败");
        }
        return "";
    }
}
