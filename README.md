# soa2
### 说明
|模块|访问|说明|
|-|-|-|
|api|-|dubbo接口|
|zk|2181|-|
|dubbo|20880|starter(可切xml)|
|service|[8080](http://localhost:8080/index) \| [druid](localhost:8080/druid)|服务，包括dubbo接口服务。访问数据库，redis|
|web|[8081](http://localhost:8081/index)|springboot(2.1.6.RELEASE)。调用dubbo接口，发送kafka消息|
|kafka|9092||
|redis|6379|starter|