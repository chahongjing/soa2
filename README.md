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
|esdemo| |es demo 使用7.12.1版本的es，注意，要和starter里的版本保持一致|
|webApp|[生产日志](http://127.0.0.1:8086/index) [logstash](http://127.0.0.1:9600) [es](http://127.0.0.1:9200) [kibana](http://127.0.0.1:5601)|elk demo|
### 多环境配置
1. maven打包时可以指定参数，如-P test,则为指定打test环境配置。
2. 运行时指定运行环境，如java -jar web-1.0.jar --spring.profiles.active=online。当使用此参数时无论maven打包为何种环境都会失效，统一以这里指定的为准

|模块|说明|
|---|---|
|web|kafka生产者|
|service-remote|kafka消费者|
|rocketmq|rocketmq|
|javaagent|agent的demo，install后执行`java -javaagent:agentTest.jar com.zjy.App`或`java -javaagent:agentTest.jar -jar agentTest.jar`<br>如果是先运行应用程序，则可以使用这些代码附加运行agent`-- 15186表示目标进程的PID；VirtualMachine vm = VirtualMachine.attach("15186");  vm.loadAgent(".../agent.jar");vm.detach();|