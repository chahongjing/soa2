package com.zjy.web.common;

import com.zjy.web.controller.IndexController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * TODO
 */
@Configuration
public class Swagger2Configuration {
    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        // 访问地址：http://项目实际地址/swagger-ui.html
        String controllerPackage = IndexController.class.getPackage().getName();
        Contact contact = new Contact("zjy", "https://github.com/chahongjing", "310510906@qq.com");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("soa2使用Swagger2构建RESTful APIs")
                .description("更多请关注https://github.com/chahongjing")
                .termsOfServiceUrl("https://github.com/chahongjing")
                .contact(contact)
                .version("1.5")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(controllerPackage))
                .paths(PathSelectors.any())
                .build();
    }
}
