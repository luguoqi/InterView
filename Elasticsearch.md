# Elasticsearch

## 简介

​		Elasticsearch 是一个分布式的开源**搜索**和**分析**引擎，适用于所有类型的数据，包括文本、数字、地理空间、结构化和非结构化数据。Elasticsearch 在 Apache Lucene 的基础上开发而成，由 Elasticsearch N.V.（即现在的 Elastic）于 2010 年首次发布。但是，你没法直接用lucene，必须自己写代码去调用他的接口。Elasticsearch 以其简单的 REST 风格 API、分布式特性、速度和可扩展性而闻名，是 Elastic Stack 的核心组件；Elastic Stack 是适用于数据采集、充实、存储、分析和可视化的一组开源工具。人们通常将 Elastic Stack 称为 ELK Stack（代指 Elasticsearch、Logstash 和 Kibana），目前 Elastic Stack 包括一系列丰富的轻量型数据采集代理，这些代理统称为 Beats，可用来向 Elasticsearch 发送数据。

中文官方文档： https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html



## 为什么不能替代MySQL？





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

## 为何使用 Elasticsearch？

**Elasticsearch 很快。** 由于 Elasticsearch 是在 **Lucene** 基础上构建而成的，所以在全文本搜索方面表现十分出色。Elasticsearch 同时还是一个近实时的搜索平台，这意味着从文档索引操作到文档变为可搜索状态之间的延时很短，一般只有一秒。因此，Elasticsearch 非常适用于对时间有严苛要求的用例，例如安全分析和基础设施监测。

**Elasticsearch 具有分布式的本质特征。** Elasticsearch 中存储的文档分布在不同的容器中，这些容器称为*分片*，可以进行复制以提供数据冗余副本，以防发生硬件故障。Elasticsearch 的分布式特性使得它可以扩展至数百台（甚至数千台）服务器，并处理 PB 量级的数据。

**Elasticsearch 包含一系列广泛的功能。** 除了速度、可扩展性和弹性等优势以外，Elasticsearch 还有大量强大的内置功能（例如数据汇总和索引生命周期管理），可以方便用户更加高效地存储和搜索数据。

**Elastic Stack 简化了数据采集、可视化和报告过程。** 通过与 Beats 和 Logstash 进行集成，用户能够在向 Elasticsearch 中索引数据之前轻松地处理数据。同时，Kibana 不仅可针对 Elasticsearch 数据提供实时可视化，同时还提供 UI 以便用户快速访问应用程序性能监测 (APM)、日志和基础设施指标等数据。

ES的存储结构可以类比MySQL

<img src="E:\IDEA-workspace\InterView\images\ES.png" alt="ES" style="zoom: 33%;" />

![ES3](E:\IDEA-workspace\InterView\images\ES3.jpg)

![ES2](E:\IDEA-workspace\InterView\images\ES2.jpg)



![ES1](E:\IDEA-workspace\InterView\images\ES1.jpg)

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
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx128m" -v /opt/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/elasticsearch.yml -v /opt/elasticsearch/data:/usr/share/elasticsearch/data -v /opt/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.4.2
# 启动Kibana可视化界面，启动完成可以访问5601，可以看到可视化界面
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.136.135:9200 -p 5601:5601 -d kibana:7.4.2

```

ElasticSearch启动报错

<img src="E:\IDEA-workspace\InterView\images\ES4.jpg" alt="ES4" style="zoom:67%;" />



访问被拒绝，此时需要将刚刚创建的elasticsearch目录下的所有文件夹权限调整为777，chmod -R 777 /opt/elasticsearch

## 2.存储操作

### 1._cat

- GET /_cat/nodes 查看所有节点
- GET /_cat/health 查看ES健康状况
- GET /_cat/master 查看主节点
- GET /_cat/indices 查看所有索引，相当于MySQL中 show databases;

### 2.索引一个文档(相当于MySQL保存一条记录)

​		保存一条数据，就是要保存在哪个索引的哪个类型下，指定用哪一个唯一标识(相当于MySQL的要保存在哪个数据库的哪张表下)

### 2.1、保存操作

​		下图是向customer索引下的external类型下保存一个docid为1的json数据(请求类型可以是POST/PUT,其有保存和更新功能)

PUT/POST  customer/external/1							body		{"name":"Tom"}				发送多次则为更新操作

<img src="E:\IDEA-workspace\InterView\images\ES5.jpg" alt="ES5" style="zoom: 67%;" />

使用POST并且不指定id，则ES会自动生成一个随机的唯一id

<img src="E:\IDEA-workspace\InterView\images\ES6.jpg" alt="ES6" style="zoom:67%;" />

### 2.2、查询操作

<img src="E:\IDEA-workspace\InterView\images\ES7.jpg" alt="ES7" style="zoom:67%;" />

_seq_no,并发控制的字段，当需要控制并发的时候加上?if_seq_no=3&if_primary_term=1,如果两个修改同时添加这两个参数，理论上只有一个能修改成功。

### 2.3、更新文档

​	如果有属性新增或减少直接变更json数据即可

POST customer/external/1/_update					body        {"doc":{"name":"Tom2"}}		对比更新，一致不更新

或者 POST/PUT	 customer/external/1		   	body        {"name":"Tom2"}		不对比，每次都更新

<img src="E:\IDEA-workspace\InterView\images\ES9.jpg" alt="ES9" style="zoom:67%;" />

### 2.4、删除文档

删除文档		DELETE 	customer/external/1

删除索引		DELETE 	customer

不能删除类型

<img src="E:\IDEA-workspace\InterView\images\ES8.jpg" alt="ES8" style="zoom: 67%;" />

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

<img src="E:\IDEA-workspace\InterView\images\ES10.jpg" alt="ES10" style="zoom:67%;" />

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

#### 3.2.1match(全文匹配)

"match": {"account_number":20}	如果匹配非字符串字段，则其为精确匹配，否则为模糊匹配

"match": {"address":"mill lane"}	此时就会分词并模糊匹配(全文检索)，查询出所有包含mill/lane/mill lane的文档，默认按评分倒叙排列

#### 3.2.2match_phrase(短语匹配)

"match_phrase": {"address":"mill lane"}或者"match": {"address.keyword":"mill lane"}	将匹配的值作为一个整体单词(不分词)进行模糊匹配，查询出所有包含mill lane的文档，但是第一种方式类似于like "%mill lane%"的形式，而第二种方式相当于= "mill lane"。

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

![image-20210103150438690](E:\IDEA-workspace\InterView\images\image-20210103150438690.png)

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
elasticsearch-plugins list
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
docker cp nginx:/etc/nginx ./nginx
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































































































