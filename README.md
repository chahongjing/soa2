# soa2
### 说明
|模块|访问|说明|
|---|---|---|
|api|-|dubbo接口|
|common|-|-|
|dao|-|mybatis-starter，扩展mapper，mybatis-generater生成基本xml|
|domain|-|mybatis-generater生成实体|
|esdemo|[【8098】](http://localhost:8098/test/findAll) [索引查询 9200](http://127.0.0.1:9200/student_index/_search?q=思想&pretty=true) [kibana 5601](http://127.0.0.1:5601)|es-starter,highlevel操作,lowlevel操作。注意es和kibana服务版本要与starter引用的es版本号一致，具体配置见[【配置】](./esdemo/README.MD)|
|javaagent|-|agent的demo，install后执行`java -javaagent:agentTest.jar com.zjy.App`或`java -javaagent:agentTest.jar -jar agentTest.jar`<br>如果是先运行应用程序，则可以使用这些代码附加运行agent`-- 15186表示目标进程的PID；VirtualMachine vm = VirtualMachine.attach("15186");  vm.loadAgent(".../agent.jar");vm.detach();|
|mp|[【8080】](http://localhost:8080) [【git docs】](https://gitee.com/baomidou/mybatisplus-spring-boot)|mybatis-plus-starter，实体属性枚举|
|multidatasource|[【8080】](http://localhost:8080)|多数据源，不同数据源走不同的mapper；打war包|
|mystater|-|mystarter|
|rocketmq|-|rocketmq-starter|
|service|-|引入dao模块，pagehelper-starter,dubbo-starter,durid-starter,cache-starter,redis-starter,zk,kafka<br>启动kafka:<br>1. zookeeper-server-start.bat ../../config/zookeeper.properties<br>2. kafka-server-start.bat ../../config/server.properties|
|service-remote|[【8080】](http://localhost:8080)|dubbo服务提供者，引用service模块，kafka消费者|
|springboot-admin|[【8085】](http://localhost:8085)|actuator监控|
|web|[【8081】](http://localhost:8081) [【druid】](http://localhost:8081/druid) [【swagger2】](http://localhost:8081/swagger-ui.html)|引用mybatis-starter，引用dubbo服务(starter和xml)，引用service模块，CommandLineRunner，swagger2, kafka生产者，druid|
|webApp|[【8086】](http://localhost:8086/index) [es](http://127.0.0.1:9200) [logstash](http://127.0.0.1:9600) [kibana](http://127.0.0.1:5601)|elk,[【相关配置】](./webApp/README.MD)|
|mongodb|[【8078】](http://localhost:8078/index)|集成mongodb，多数据源|
|security|[【8102】](http://localhost:8102/login) [【8103】](http://localhost:8103/testPage)|server:sso登录服务;client：客户端，未登录会跳转到server登录;common：公共模块,集成spring-security，供server和client共用|
|jwt|-|jwt相关demo,集成spring-security|

### 多环境配置
1. maven打包时可以指定参数，如-P test,则为指定打test环境配置。
2. 运行时指定运行环境，如java -jar web-1.0.jar --spring.profiles.active=online。当使用此参数时无论maven打包为何种环境都会失效，统一以这里指定的为准
