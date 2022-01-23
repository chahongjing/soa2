### es
1. 编辑配置文件：`/etc/elasticsearch/elasticsearch.yml`。添加对外提供服务的地址和端口
``` yml
network.host: 127.0.0.1
http.port: 9200
xpack.ml.enabled: false
```
2. 启动es：`/usr/share/elasticsearch/bin/elasticsearch`。然后访问服务：[es](http://127.0.0.1:9200)
3. [索引查询](http://127.0.0.1:9200/student_index/_search?q=思想&pretty=true) 其中student_index为索引名称，q为搜索关键字，pretty为格式化输出，默认为json
4. 命令行查询`curl -X POST -d '{"query":{"term":{"address.area.keyword":"南漳县"}}}' --header "Content-Type: application/json" http://localhost:9200/student_index/_search/`
### kibana
1. 访问本地es配置。打开文件config/kibana.yml。放开`elasticsearch.hosts: ["http://localhost:9200"]`的注释;修改中文显示`i18n.locale: "zh-CN"`
2. 启动kibana:`/usr/share/kibana/bin/kibana`。然后访问服务：[kibana](http://127.0.0.1:5601)