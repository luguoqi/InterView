# Elasticsearch

## 简介

​		Elasticsearch 是一个分布式的开源**搜索**和**分析**引擎，适用于所有类型的数据，包括文本、数字、地理空间、结构化和非结构化数据。Elasticsearch 在 Apache Lucene 的基础上开发而成，由 Elasticsearch N.V.（即现在的 Elastic）于 2010 年首次发布。但是，你没法直接用lucene，必须自己写代码去调用他的接口。Elasticsearch 以其简单的 REST 风格 API、分布式特性、速度和可扩展性而闻名，是 Elastic Stack 的核心组件；Elastic Stack 是适用于数据采集、充实、存储、分析和可视化的一组开源工具。人们通常将 Elastic Stack 称为 ELK Stack（代指 Elasticsearch、Logstash 和 Kibana），目前 Elastic Stack 包括一系列丰富的轻量型数据采集代理，这些代理统称为 Beats，可用来向 Elasticsearch 发送数据。

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230021533.png" alt="image-20210111230021533" style="zoom: 67%;" />

**Elastic Stack成员详解**

**ElasticSearch：**

ElasticSearch基于Java，是一个开源分布式搜索引擎，它的特点有：分布式、零配置、自动发现、索引自动分片、索引副本机制、restful风格接口、多数据源、自动搜索负载等。

**Logstash：**

Logstash基于Java，是一个开源的用于收集分析和存储日志的工具

**Kibana：**

Kibana基于nodejs，也是一个开源免费的工具，Kibana可以为Logstash和ElasticSearch提供有好的Web界面，可以汇总、分析和搜索重要的数据日志。

**Beats：**

Beats是elastic公司开源的一款采集系统监控数据的代理agent，是在被监控服务器上以客户端的形式运行的数据收集器的统称，可以直接把数据发送给ElasticSearch或者通过Logstash发送给ElasticSearch，然后进行后续的数据分析活动。



### 相关资料：

中文官方文档(非常老)： https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html

英文文档：https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html

github：https://github.com/elastic/elasticsearch

系统学习ElasticSearch：https://www.zhihu.com/column/TeHero

入门ElasticSearch：https://blog.csdn.net/qq_21046965/category_8659190.html



## ElasticSearch、MySQL对比

MySQL：关系型数据库，主要面向OLTP，支持事务，支持二级索引，支持sql，支持主从、Group Replication架构模型（本文全部以Innodb为例，不涉及别的存储引擎）。

ElasticSearch：ES是一款分布式的全文检索框架，底层基于Lucene实现，天然分布式，p2p架构，不支持事务及复杂的关系，采用倒排索引提供全文检索。

MySQL中要提前定义表结构，也就是说表共有多少列（属性）需要提前定义好，并且同时需要定义好每个列所占用的存储空间。数据以行为单位组织在一起的，假如某一行的某一列没有数据，也需要占用存储空间。

ES:比较灵活，索引中的field类型可以提前定义（定义mapping），也可以不定义，如果不定义，会有一个默认类型，不过出于可控性考虑，关键字段最好提前定义好。不同的是，ES存的是倒排索引，

​		https://blog.csdn.net/weixin_41247759/article/details/105783030

​		https://www.cnblogs.com/luxiaoxun/p/5452502.html

## ElasticSearch、Solr、Lucene对比

- Lucene

Lucene是apache下的一个子项目，是一个开放源代码的全文检索引擎工具包，但它不是一个完整的全文检索引擎，而是一个全文检索引擎的架构，提供了完整的查询引擎和索引引擎，部分文本分析引擎。官网地址：https://lucene.apache.org/

- Solr

Solr是一个高性能，采用Java5开发，基于Lucene的全文搜索服务器。同时对其进行了扩展，提供了比Lucene更为丰富的查询语言，同时实现了可配置、可扩展并对查询性能进行了优化，并且提供了一个完善的功能管理界面，是一款非常优秀的全文搜索引擎。官网地址：http://lucene.apache.org/solr/

- Elasticsearch

Elasticsearch跟Solr一样，也是一个基于Lucene的搜索服务器，它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。官网地址：https://www.elastic.co/products/elasticsearch

### 1、Elasticsearch的优缺点：

- 优点：

1.Elasticsearch是分布式的。不需要其他组件，分发是实时的，被叫做"Push replication"。

2.Elasticsearch 完全支持 Apache Lucene 的接近实时的搜索。

3.处理多租户（multitenancy）不需要特殊配置，而Solr则需要更多的高级设置。

4.Elasticsearch 采用 Gateway 的概念，使得完备份更加简单。

5.各节点组成对等的网络结构，某些节点出现故障时会自动分配其他节点代替其进行工作。

- 缺点：

1.只有一名开发者（当前Elasticsearch GitHub组织已经不只如此，已经有了相当活跃的维护者）

2.还不够自动（不适合当前新的Index Warmup API）

### 2、Solr的优缺点：

- 优点

1.Solr有一个更大、更成熟的用户、开发和贡献者社区。

2.支持添加多种格式的索引，如：HTML、PDF、微软 Office 系列软件格式以及 JSON、XML、CSV 等纯文本格式。

3.Solr比较成熟、稳定。

4.不考虑建索引的同时进行搜索，速度更快。

- 缺点

1.建立索引时，搜索效率下降，实时索引搜索效率不高。

### 3、Lucene的优缺点

说明：Lucene 是一个 JAVA 搜索类库，它本身并不是一个完整的解决方案，需要额外的开发工作。

优点：成熟的解决方案，有很多的成功案例。apache 顶级项目，正在持续快速的进步。庞大而活跃的开发社区，大量的开发人员。它只是一个类库，有足够的定制和优化空间：经过简单定制，就可以满足绝大部分常见的需求；经过优化，可以支持 10亿+ 量级的搜索。

缺点：需要额外的开发工作。所有的扩展，分布式，可靠性等都需要自己实现；非实时，从建索引到可以搜索中间有一个时间延迟，而当前的“近实时”(Lucene Near Real Time search)搜索方案的可扩展性有待进一步完善

### 4、Elasticsearch 与 Solr 的比较：

1.二者安装都很简单；

2.Solr 利用 Zookeeper 进行分布式管理，而 Elasticsearch 自身带有分布式协调管理功能;

3.Solr 支持更多格式的数据，而 Elasticsearch 仅支持json文件格式；

4.Solr 官方提供的功能更多，而 Elasticsearch 本身更注重于核心功能，高级功能多有第三方插件提供；

5.Solr 在传统的搜索应用中表现好于 Elasticsearch，但在处理实时搜索应用时效率明显低于 Elasticsearch。

6.Solr 是传统搜索应用的有力解决方案，但 Elasticsearch 更适用于新兴的实时搜索应用。

​	**当单纯的对已有数据进行搜索时，Solr更快。**

​	**当实时建立索引时, Solr会产生io阻塞，查询性能较差, Elasticsearch具有明显的优势。**

​	**随着数据量的增加，Solr的搜索效率会变得更低，而Elasticsearch却没有明显的变化。**

综上所述，Solr的架构不适合实时搜索的应用。

​		https://zhuanlan.zhihu.com/p/78309627



## Elasticsearch 的用途是什么？

Elasticsearch 在速度和可扩展性方面都表现出色，而且还能够索引多种类型的内容，这意味着其可用于多种用例：

- 应用程序搜索
- 网站搜索
- 企业搜索
- 日志处理和分析
- 基础设施指标和容器监测
- 应用程序性能监测
- 地理空间数据分析和可视化
- 安全分析
- 业务分析

## Elasticsearch 的工作原理是什么？

原始数据会从多个来源（包括日志、系统指标和网络应用程序）输入到 Elasticsearch 中。*数据采集*指在 Elasticsearch 中进行*索引*之前解析、标准化并充实这些原始数据的过程。这些数据在 Elasticsearch 中索引完成之后，用户便可针对他们的数据运行复杂的查询，并使用聚合来检索自身数据的复杂汇总。在 Kibana 中，用户可以基于自己的数据创建强大的可视化，分享仪表板，并对 Elastic Stack 进行管理。

倒排索引：也叫反向索引，有反向索引就有有正向索引。通俗地来讲，正向索引是通过key找value，反向索引则是通过value找key。



## Elasticsearch 索引是什么？

Elasticsearch *索引*指相互关联的文档集合。Elasticsearch 会以 JSON 文档的形式存储数据。每个文档都会在一组*键*（字段或属性的名称）和它们对应的值（字符串、数字、布尔值、日期、*数值*组、地理位置或其他类型的数据）之间建立联系。

Elasticsearch 使用的是一种名为*倒排索引*的数据结构，这一结构的设计可以允许十分快速地进行全文本搜索。倒排索引会列出在所有文档中出现的每个特有词汇，并且可以找到包含每个词汇的全部文档。

在索引过程中，Elasticsearch 会存储文档并构建倒排索引，这样用户便可以近实时地对文档数据进行搜索。索引过程是在索引 API 中启动的，通过此 API 您既可向特定索引中添加 JSON 文档，也可更改特定索引中的 JSON 文档。

倒排索引1：https://zhuanlan.zhihu.com/p/33671444?utm_source=wechat_timeline

倒排索引2：https://zhuanlan.zhihu.com/p/137916758

## 为何使用 Elasticsearch？

**Elasticsearch 很快。** 由于 Elasticsearch 是在 **Lucene** 基础上构建而成的，所以在全文本搜索方面表现十分出色。Elasticsearch 同时还是一个近实时的搜索平台，这意味着从文档索引操作到文档变为可搜索状态之间的延时很短，一般只有一秒。因此，Elasticsearch 非常适用于对时间有严苛要求的用例，例如安全分析和基础设施监测。

**Elasticsearch 具有分布式的本质特征。** Elasticsearch 中存储的文档分布在不同的容器中，这些容器称为*分片*，可以进行复制以提供数据冗余副本，以防发生硬件故障。Elasticsearch 的分布式特性使得它可以扩展至数百台（甚至数千台）服务器，并处理 PB 量级的数据。

**Elasticsearch 包含一系列广泛的功能。** 除了速度、可扩展性和弹性等优势以外，Elasticsearch 还有大量强大的内置功能（例如数据汇总和索引生命周期管理），可以方便用户更加高效地存储和搜索数据。

**Elastic Stack 简化了数据采集、可视化和报告过程。** 通过与 Beats 和 Logstash 进行集成，用户能够在向 Elasticsearch 中索引数据之前轻松地处理数据。同时，Kibana 不仅可针对 Elasticsearch 数据提供实时可视化，同时还提供 UI 以便用户快速访问应用程序性能监测 (APM)、日志和基础设施指标等数据。

ES的存储结构可以类比MySQL

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111225142361.png" alt="image-20210111225142361" style="zoom: 50%;" />

## 使用场景

（1）维基百科，类似百度百科，全文检索，高亮，搜索推荐

（2）The Guardian（国外新闻网站），用户行为日志（点击，浏览，收藏，评论）+社交网络数据，数据分析

（3）Stack Overflow（国外的程序异常讨论论坛）

（4）GitHub（开源代码管理）

（5）电商网站，检索商品

（6）日志数据分析，logstash采集日志，ES进行复杂的数据分析（ELK技术，elasticsearch+logstash+kibana）

（7）商品价格监控网站

（8）BI系统，商业智能，Business Intelligence。

（9）站内搜索（电商，招聘，门户，等等），IT系统搜索（OA，CRM，ERP，等等），数据分析（ES热门的一个使用场景）

使用场景1：https://developer.aliyun.com/article/707000

使用场景2：https://my.oschina.net/90888/blog/1619325

ES实现百亿级数据实时分析：https://www.jianshu.com/p/ddfff7e45822

## 1.安装

```shell
# 拉取镜像
docker pull elasticsearch:7.4.2
docker pull kibana:7.4.2
# 创建配置文件和数据的挂载目录
mkdir -p /opt/elasticsearch/config
mkdir -p /opt/elasticsearch/data
mkdir -p /opt/elasticsearch/plugins
#配置远程访问
echo "http.host: 0.0.0.0" >> /opt/elasticsearch/config/elasticsearch.yml
#启动elasticsearch,其中9200是ES提供restful接口的端口，9300是ES在分布式集群状态下节点间通信端口,注意如果这里不指定虚拟机内存，ES则会直接占用全部内存。启动完成后可以访问9200，返回json则启动成功
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms1024m -Xmx1024m" -v /opt/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/elasticsearch.yml -v /opt/elasticsearch/data:/usr/share/elasticsearch/data -v /opt/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.4.2
#注意如果此时启动失败报错访问被拒绝则可能是刚刚新建的配置文件没有权限导致的，这是需要修改配置文件权限
chmod -R 777 /opt/elasticsearch
# 启动Kibana可视化界面，启动完成可以访问5601，可以看到可视化界面
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.136.135:9200 -p 5601:5601 -d kibana:7.4.2

```

## 2.存储操作

### 1._cat

官网地址：https://www.elastic.co/guide/en/elasticsearch/reference/current/cat.html

- GET /_cat/nodes 查看所有节点(集群状态、内存、磁盘使用状态)，添加?v参数查看

  ip         heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
  172.17.0.3           21          84   3    0.01    0.39     0.47 dilm      *      41081103b675

- GET /_cat/health 查看ES健康状况

  epoch      timestamp cluster        status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
  1610375467 14:31:07  docker-cluster yellow          1         1      5   5    0    0        2             0                  -                 71.4%

- GET /_cat/master 查看主节点

  id                     host       ip         node
  AQNum6sfQs6VqJuIlH-sxQ 172.17.0.3 172.17.0.3 41081103b675

- GET /_cat/indices 查看所有索引，相当于MySQL中 show databases;

  health status index                    uuid                   pri rep docs.count docs.deleted store.size pri.store.size
  yellow open   bank                     9xDEXQekSDqC_aDlf2t02g   1   1       1000            0    429.1kb        429.1kb
  green  open   .kibana_task_manager_1   sQ9kajZvRGu9PWYnlROR1A   1   0          2            1     15.6kb         15.6kb
  green  open   .apm-agent-configuration 8LXJKfdpTp6udnMgbkPl1g   1   0          0            0       283b           283b
  green  open   .kibana_1                Y1RA0vUyTsWRM113DA57hQ   1   0          8            0     28.6kb         28.6kb
  yellow open   customer                 f3ZmCwf3Q46UQ4kNiVFRQA   1   1          5            3       16kb           16kb

### 2.索引一个文档(相当于MySQL保存一条记录)

​		保存一条数据，就是要保存在哪个索引的哪个类型下，指定用哪一个唯一标识(相当于MySQL的要保存在哪个数据库的哪张表下)

### 2.1、保存操作

​		下图是向customer索引下的external类型下保存一个docid为1的json数据(请求类型可以是POST/PUT,其有保存和更新功能)

PUT/POST  customer/external/1							body		{"name":"Tom"}				发送多次则为更新操作

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230115172.png" alt="image-20210111230115172" style="zoom:67%;" />

使用POST并且不指定id，则ES会自动生成一个随机的唯一id

<img src="https://gitee.com/img/20210110202952.jpg" alt="ES6" style="zoom:67%;" />

### 2.2、查询操作

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230155189.png" alt="image-20210111230155189" style="zoom:67%;" />

_seq_no,并发控制的字段，当需要控制并发的时候加上?if_seq_no=3&if_primary_term=1,如果两个修改同时添加这两个参数，理论上只有一个能修改成功。

### 2.3、更新文档

​	如果有属性新增或减少直接变更json数据即可

POST customer/external/1/_update					body        {"doc":{"name":"Tom2"}}		对比更新，一致不更新

或者 POST/PUT	 customer/external/1		   	body        {"name":"Tom2"}		不对比，每次都更新

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230219573.png" alt="image-20210111230219573" style="zoom:67%;" />

### 2.4、删除文档

删除文档		DELETE 	customer/external/1

删除索引		DELETE 	customer

不能删除类型

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230251413.png" alt="image-20210111230251413" style="zoom: 67%;" />

### 2.5、批量API

需在kibana中操作，并且每条记录都是独立的，某条记录的操作失败不会影响其他记录

​	POST	/customer/external/_bulk

​		{"index":{"_id":"1"}}_

​		{"name":"Tom"}

​		{"index":{"_id":"2"}}

​		{"name":"Tom2"}

语法格式(两行为一组，action可以是新增、删除、更新)

{action:{metadata}}

{requestbody}

{action:{metadata}}

{requestbody}

![image-20210111230339276](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230339276.png)

复杂批量

```json
POST	/_bulk
{"delete":{"_index":"customer","_type":"external","_id":"1"}}
{"create":{"_index":"customer","_type":"external","_id":"1"}}
{"name":"Tom"}
{"index":{"_index":"customer","_type":"external"}}
{"name":"Tom2"}
{"update":{"_index":"customer","_type":"external","_id":"1"}}
{"doc":{"name":"Tom3"}}
```

相关测试数据 /bank/account/_bulk   [accounts.zip](images\accounts.zip) 

## 3.检索操作

### 3.1 _search体验

查询所有bank索引下的数据，并按account_number升序排列

GET	/bank/_search?q=*&sort=account_number:asc

或者使用Query DSL方式		GET /bank/_search		{"query": { "match_all": {} },"sort": [{ "account_number": "asc" }]}

一般的Query DSL语法

```console
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [		#排序
    { "account_number": "asc" }	
  ],
  "from": 10,# 第几条开始
  "size": 10, #共查询几条
  "_source":["balance"]	#只返回指定字段
}
```

### 3.2Query DSL相关字段

Query DSL(Domain Specific Language) 领域特定语言

官网地址：https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html#query-dsl

#### 3.2.1match(全文匹配)

"match": {"account_number":20}	如果匹配非字符串字段，则其为精确匹配，否则为模糊匹配

"match": {"address":"mill lane"}	此时就会分词并模糊匹配(全文检索)，查询出所有包含mill/lane/mill lane的文档，默认按评分倒叙排列

#### 3.2.2match_phrase(短语匹配)

"match_phrase": {"addres s":"mill lane"}或者"match": {"address.keyword":"mill lane"}	将匹配的值作为一个整体单词(不分词)进行模糊匹配，查询出所有包含mill lane的文档，但是第一种方式类似于like "%mill lane%"的形式，而第二种方式相当于= "mill lane"。

#### 3.2.3 multi_match(多字段匹配)

"multi_match": {"query":"mill lane","fields":["state","address"]}	查询state或者address中包含mill/lane/mill lane的文档，并且不区分大小写

#### 3.2.4 bool(复合查询)

"bool":{"must":[{"match":{"age":"40" }}],"must_not":[{"match":{"state":"ID"}}],"should":{"match":{"lastname":"Tom"}}}	查询匹配age为40并且state不包含ID的文档，并且不区分大小写,should不影响条件匹配，但是影响最终的评分。注意must_not不影响评分

#### 3.2.5 filter(结果过滤)

"bool":{"must":[{"filter":{"range":{"balance":{"gte":20000,"lte":30000}}}}]}	查询balance在20000至30000之间的数据，其功能类似于must，但是filter不影响评分，所以此时所有的分值都是0.0

#### 3.2.6 term

和match一样，匹配某个属性的值，全文检索字段用match，其他非text字段匹配用term，也就是说精确值的匹配使用term，例如age、balance

"term": {"age":18}	查询出age为18的文档

"term": {"address":"Kings"}	则查询不出数据

#### 3.2.7 aggregations(聚合)

聚合提供了从数据中分组和提取数据的能力，最简单的聚合方法大致等于SQL GROUP BY和SQL聚合函数，相当于把检索出的数据做一些分析，例如我们想查询平均年龄、年龄分布等等。

![image-20210111230459819](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210111230459819.png)

#### 3.2.8 mapping (映射)

表示json数据中每个数据的类型，比如 text、long、integer、ip等等。

GET /bank/_mapping  查询每个字段的类型，在7.X中已经移除类型的定义

#### 3.2.9 分词

将一段完整的字符串分成一个个单词/词语，然后对其进行索引，方便后期查询。

```console
#测试
POST _analyze
{
  "tokenizer": "standard",
  "text": "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."
}
#结果
[ The, 2, QUICK, Brown, Foxes, jumped, over, the, lazy, dog's, bone ]
#但是如果使用“中国大学”
#显然不是我们想要的结果，这是因为ES默认使用标准分词器(standard)进行分词
[ 中,国,大,学 ]
```

安装ik分词器  https://github.com/medcl/elasticsearch-analysis-ik

```shell
#1、进入es容器中的plugins目录中,因为我们安装es时已经做了数据卷映射，索引也不用进入容器中
# cd /opt/elasticsearch/plugins
docker exec -it elasticsearch /bin/bash
cd plugins
# 下载ik分词器
wget https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.4.2/elasticsearch-analysis-ik-7.4.2.zip
#新建文件夹
mkdir ik
#解压
unzip -d ik elasticsearch-analysis-ik-7.4.2.zip
#这时候可以进入容器的中ES的bin目录执行,可以看到ik插件已经安装完成
elasticsearch-plugin list
#重启ES
docker restart elasticsearch
```

```json
#测试1
POST _analyze
{
  "tokenizer": "ik_smart",
  "text": "我是中国人"
}
#结果
[我,是,中国人]
#测试2
POST _analyze
{
  "tokenizer": "ik_smart",
  "text": "我是中国人"
}
#结果，最大的单词组合
[我,是,中国人,中国,国人]
```

指定分词

```shell
#安装nginx，先随便安装一个nginx，目的是复制nginx的配置文件
docker run -p 80:80 --name nginx -d nginx:1.10
#将容器中的文件复制到宿主机目录中
docker cp nginx:/etc/nginx ./
#最终的目录结构/opt/nginx/conf
docker run -p 80:80 --name nginx -v /opt/nginx/html:/usr/share/nginx/html -v /opt/nginx/logs:/var/log/nginx -v /opt/nginx/conf:/etc/nginx -d nginx:1.10
#在nginx的html下新建fenci.txt文件，内容为：我是
#修改ik分词器的配置文件
vim /opt/elasticsearch/plugins/ik/config/IKAnalyzer.cfg.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
        <comment>IK Analyzer 扩展配置</comment>
        <!--用户可以在这里配置自己的扩展字典 -->
        <entry key="ext_dict"></entry>
         <!--用户可以在这里配置自己的扩展停止词字典-->
        <entry key="ext_stopwords"></entry>
        <!--用户可以在这里配置远程扩展字典 -->
        <entry key="remote_ext_dict">http://192.168.136.135/es/fenci.txt</entry>
        <!--用户可以在这里配置远程扩展停止词字典-->
        <entry key="remote_ext_stopwords">words_location</entry>
</properties>
#重启es
docker restart elasticsearch

#然后再次测试我是中国人
POST _analyze
{
  "tokenizer": "ik_max_word",
  "text": "我是中国人"
}
#结果，最大的单词组合，此时我是就作为一个词组来进行分词
[我是,中国人,中国,国人]

```



## 4.使用java操作ES

官网地址(https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html)

客户端对比

1.JestClient：非官方、更新慢

2.RestTemplate/HttpClient：模拟发送Http请求，但ES很多操作都需要自己封装，比较麻烦

3.Elasticsearch-Rest-Client：官方客户端，封装了ES操作，API层次分明，简单易用，推荐使用。

Elasticsearch-Rest-Client：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-overview.html

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.10.1</version>
</dependency>
```

























































































