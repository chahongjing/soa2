# soa2
### 说明
|模块|访问|说明|
|---|---|---|
|api|-|dubbo接口|
|zk|2181|-|
|dubbo|20880|starter(可切xml)|
|service|[【8080】](http://localhost:8080/index)|服务，包括dubbo接口服务。访问数据库，redis|
|web|[【8081】](http://localhost:8081/index) [【druid】](http://localhost:8081/druid) [【swagger2】](http://localhost:8081/swagger-ui.html)|springboot(2.1.6.RELEASE)。调用dubbo接口，发送kafka消息|
|kafka|9092|1. zookeeper-server-start.bat ../../config/zookeeper.properties<br>2. kafka-server-start.bat ../../config/server.properties |
|redis|6379|starter|
|mystarter| |自定义starter|
|springboot-admin|[【8085】](http://localhost:8085)||
|mybatis-plus-starter|[【git docs】](https://gitee.com/baomidou/mybatisplus-spring-boot)|||
|dubbo-admin|||
|[multidatasource 8080](http://localhost:8080)| |多数据源配置，打war包|
|esdemo| |es demo 使用6.2.2版本的es|
### 多环境配置
1. maven打包时可以指定参数，如-P test,则为指定打test环境配置。
2. 运行时指定运行环境，如java -jar web-1.0.jar --spring.profiles.active=online。当使用此参数时无论maven打包为何种环境都会失效，统一以这里指定的为准
