package com.zjy.mystarter;

import com.zjy.api.TestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MyProperties.class)
@ConditionalOnClass(TestApi.class)
public class MyAutoConfiguration {

    @Autowired
    private MyProperties myProperties;

    public MyAutoConfiguration() {
        System.out.println("construct");
    }

    @Bean
    @ConditionalOnMissingBean(TestApi.class)
    @ConditionalOnProperty(prefix = "my", name = "enabletwo", havingValue = "false",matchIfMissing = true)
    public TestApi myServiceOne() {
        System.out.println("service one");
        TestApi myService = new MyApiOne();
        myService.setName(myProperties.getName() + " from mystarter: " + myService.getClass().getName());
        return myService;
    }

    @Bean
    @ConditionalOnMissingBean(TestApi.class)
    @ConditionalOnProperty(prefix = "my", name = "enabletwo", havingValue = "true")
    public TestApi myServiceTwo() {
        System.out.println("service two");
        TestApi myService = new MyApiTwo();
        myService.setName(myProperties.getName() + " from mystarter: " + myService.getClass().getName());
        return myService;
    }
}