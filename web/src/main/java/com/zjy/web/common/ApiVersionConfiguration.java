package com.zjy.web.common;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * TODO
 */
public class ApiVersionConfiguration {}
//@Configuration
//public class ApiVersionConfiguration extends WebMvcConfigurationSupport {
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
////        registry.addInterceptor(new MyApiInterceptor());
//    }
//
////    @Override
////    @Bean
////    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
////        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
////        handlerMapping.setOrder(0);
////        handlerMapping.setInterceptors(getInterceptors());
////        return handlerMapping;
////    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        /*
//        1.需要先定义一个convert转换消息的对象；
//        2.添加fastjson的配置信息，比如是否要格式化返回的json数据
//        3.在convert中添加配置信息
//        4.将convert添加到converters中
//         */
//        //1.定义一个convert转换消息对象
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        //2.添加fastjson的配置信息，比如：是否要格式化返回json数据
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(
//                SerializerFeature.PrettyFormat
//        );
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        converters.add(fastConverter);
//    }
//}


//@Target({ElementType.METHOD,ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Mapping
//public @interface ApiVersion {
//
//    /**
//     * 标识版本号
//     * @return
//     */
//    int value();
//}
//public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
//
//    // 路径中版本的前缀， 这里用 /v[1-9]/的形式
//    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
//
//    private int apiVersion;
//
//    public ApiVersionCondition(int apiVersion){
//        this.apiVersion = apiVersion;
//    }
//
//    @Override
//    public ApiVersionCondition combine(ApiVersionCondition other) {
//        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
//        return new ApiVersionCondition(other.getApiVersion());
//    }
//
//    @Override
//    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
//        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
//        if(m.find()){
//            Integer version = Integer.valueOf(m.group(1));
//            if(version >= this.apiVersion)
//            {
//                return this;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
//        // 优先匹配最新的版本号
//        return other.getApiVersion() - this.apiVersion;
//    }
//
//    public int getApiVersion() {
//        return apiVersion;
//    }
//}
//public class CustomRequestMappingHandlerMapping extends
//        RequestMappingHandlerMapping {
//
//    @Override
//    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
//        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
//        return createCondition(apiVersion);
//    }
//
//    @Override
//    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
//        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
//        return createCondition(apiVersion);
//    }
//
//    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
//        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
//    }
//}
//@SpringBootConfiguration
//public class WebConfig extends WebMvcConfigurationSupport {
//
//    @Bean
//    public AuthInterceptor interceptor() {
//        return new AuthInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor());
//    }
//
//    @Override
//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
//        handlerMapping.setOrder(0);
//        handlerMapping.setInterceptors(getInterceptors());
//        return handlerMapping;
//    }
//}
//
//@ApiVersion(1)
//@RequestMapping("{version}/dd")
//public class HelloController{}