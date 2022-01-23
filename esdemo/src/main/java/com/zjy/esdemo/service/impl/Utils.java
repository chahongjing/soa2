package com.zjy.esdemo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Utils {
    public static String getJsonByTemplate(String templatePath) {
        try {
            Resource resource1 = new ClassPathResource(templatePath);
//            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            byte[] bytes = IOUtils.toByteArray(resource1.getInputStream());
            String result = new String(bytes, StandardCharsets.UTF_8.name());
            return result;
        } catch (Exception e) {
            log.error("loadTestData: 读取失败");
        }
        return "";
    }
}
