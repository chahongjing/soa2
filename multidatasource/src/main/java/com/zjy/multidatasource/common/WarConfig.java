package com.zjy.multidatasource.common;

import com.zjy.multidatasource.MultidatasourceApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 打war包配置
 *
 * @author junyi.zeng@dmall.com
 * @date 2019-07-29 17:42:00
 */
public class WarConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //此处的Application.class为带有@SpringBootApplication注解的启动类
        return builder.sources(MultidatasourceApplication.class);
    }
}
