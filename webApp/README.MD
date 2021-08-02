### logstash
1. 进入`/usr/share/logstash/bin`添加配置文件`logstash.conf`
``` conf
input {
  tcp {
    #模式选择为server
    mode => "server"
    #ip和端口根据自己情况填写，端口默认4560,对应下文logback.xml里appender中的destination
    host => "127.0.0.1"
    port => 4560
    #格式json
    codec => json_lines
  }
}
filter {
  #过滤器，根据需要填写
}
output {
  elasticsearch {
    action => "index"
    #这里是es的地址，多个es要写成数组的形式
    hosts  => "127.0.0.1:9200"
    #用于kibana过滤，可以填项目名称
    index  => "applog"
  }
}
```
2. 编辑配置文件`/etc/logstash/logstash.yml`，添加logstash日志到es的配置`elasticsearch.url: http://127.0.0.1:9200`
3. 启动logstash:`/usr/share/logstash/bin/logstash -f /usr/share/logstash/bin/logstash.conf`。然后访问服务：[logstash](http://127.0.0.1:9600)
### es
1. 编辑配置文件：`/etc/elasticsearch/elasticsearch.yml`。添加对外提供服务的地址和端口
``` yml
network.host: 127.0.0.1
http.port: 9200
```
2. 启动es：`/usr/share/elasticsearch/bin/elasticsearch`。然后访问服务：[es](http://127.0.0.1:9200)
### kibana
1. 启动kibana:`/usr/share/kibana/bin/kibana`。然后访问服务：[kibana](http://127.0.0.1:5601)

### 配置
1. 首次访问，先在访问服务生成日志，再访问kibana-->discover，填写logstash中配置(output)的日志index:applog，下一步选择@timestamp,保存。然后再进入discory就可以看到日志了