# xmlSpringCloud&SpringCloud Alibaba

## 简介

微服务：是一种架构模式，他提倡将单一引用程序划分成一组小的服务，服务之间互相协调、互相配合，为用户提供最终价值。每个服务运行在其独立的进程中，服务与服务之间采用轻量级的通信机制互相协作(通常是基于Http协议的RESTfu API)。每个服务都围绕着具体业务进行构建，并且能够被独立部署到生产环境、类生产环境等。另外，应当尽量避免统一的、集中式的服务管理机制，对具体的一个服务而言，应根据业务上下文，选择合适的语言、工具对其进行构建。

springcloud：分布式微服务架构的一站式解决方案，是多种微服务架构落地技术的集合体，俗称微服务全家桶。

![image-20210117134512485](https://gitee.com/img/20210121223132.png)

springboot与springcloud版本对应关系

![image-20210117140210475](https://gitee.com/img/20210121223130.png)

更详细版本对应关系：https://start.spring.io/actuator/info

本次学习使用版本关系

|       cloud       |    Hoxton.SR1     |
| :---------------: | :---------------: |
|     **boot**      | **2.2.2.RELEASE** |
| **cloud alibaba** | **2.1.0.RELEASE** |
|     **Java**      |     **Java8**     |
|     **Maven**     |   **3.5及以上**   |
|     **MySQL**     |   **5.7及以上**   |

cloud官网手册：https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/

中文翻译：https://www.bookstack.cn/read/spring-cloud-docs/docs-index.md

boot官网手册：https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/

约定>配置>编码

设置maven

<img src="https://gitee.com/img/20210121223125.png" alt="image-20210117213642010" style="zoom:67%;" />

设置java编译版本

<img src="https://gitee.com/img/20210121223122.png" alt="image-20210117213736019" style="zoom:67%;" />

设置idea编码

<img src="https://gitee.com/img/20210121223120.png" alt="image-20210117213426194" style="zoom: 67%;" />

设置idea不展示无关文件

<img src="https://gitee.com/img/20210121223117.png" alt="image-20210117213459700" style="zoom:67%;" />

设置idea Autoware提示错误

<img src="https://gitee.com/img/20210121223115.png" alt="image-20210117213609250" style="zoom:67%;" />

设置DevTools

1.引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

2.在父工程中配置插件

```xml
<build>
    <finalName>插件配置</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork>
                <addResources>true</addResources>
            </configuration>
        </plugin>
    </plugins>
</build>
```

3.设置自动构建相关

<img src="https://gitee.com/img/20210121223112.png" alt="image-20210117213927730" style="zoom:67%;" />

4.勾选自动运行(Ctrl+Shift+Alt+/	之后选择Registry..)

<img src="https://gitee.com/img/20210121223109.png" alt="image-20210117214504844" style="zoom:67%;" />

5.重启idea并测试

​	重启后修改代码，发现此时控制台在自动重启

## Eureka(停更)

主要步骤：

​		1.新建module；2.修改POM，添加依赖和插件及包管理；3.编写YML配置文件；4.编写主启动类；5.编写业务类

相同代码重构：

![image-20210118223450691](https://gitee.com/img/20210121223058.png)

服务治理：

​		在传统的rpc远程过程调用框架中，管理每个服务之间依赖关系比较复杂，出现一个服务调用N多个服务，所以需要服务治理，管理服务与服务之间的依赖关系，实现服务调用、负载均衡、容错等，实现服务发现与注册。

SpringCloud封装了Netflix公司开发的Eureka模块来实现服务治理

服务注册与发现：Eureka采用了CS的设计架构，Eureka Server作为服务注册功能的服务器，他是服务注册中心。而系统中其他微服务，使用Eureka的客户端连接到Eureka Server并维持心跳连接。这样系统的维护人员就可以通过Eureka Server来监控系统中各个微服务是否正常运行。在服务注册与发现中，有一个注册中心，当服务启动的时候，会把当前自己服务器的信息，比如服务地址、通讯地址等以别名方式注册到注册中心上，另一方(消费者/服务提供者)，以该别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现本地RPC。调用RPC远程调用框架核心设计思想：在于注册中心，注册中心管理每个服务与服务之间的一个依赖关系(服务治理)，在任何rpc远程框架中，都会有一个注册中心(存放服务地址相关信息(接口地址))

Eureka分为Server端和Client端，

 **Eureka Server：提供服务注册服务**

 各个微服务节点通过配置启动后，会在 Eureka Server 中进行注册， 这样 Eureka Server 中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观看到。

**Eureka Client：通过服务注册中心访问**

 是一个Java客户端，用于简化Eureka Server的交互,客户端同时也具备一个内置的、 使用轮询（round-robin）负载 算法的负载均衡器在应用启动后，将会向Eureka Server发送心跳（默认周期为30秒）。如果 Eureka Server 在多个心跳周期内没有接收到某个节点的心跳，Eureka Server 将会从服务注册表中把这个服务节点移除（默认90秒）。

搭建Eureka Server

1.新建module

<img src="https://gitee.com/img/20210121223015.png" alt="image-20210121222934574" style="zoom:80%;" />

2.修改POM文件

```xml
<dependencies>
    <!--eureka-server依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <!--自定义api通用包-->
    <dependency>
        <groupId>com.yango.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <!--boot web acctuator-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

3.编写YML配置文件(新增application.yml)

```yml
server:
  port: 7001
eureka:
  instance:
    hostname: localhost #Eureka服务端实例名称
  client:
    register-with-eureka: false #false表示不向注册中心注册自己
    fetch-registry: true #表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
```

4.编写主启动类

```java
@SpringBootApplication
@EnableEurekaServer     //表示当前服务就是Eureka的服务端注册中心
public class EurekaMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7001.class, args);
    }
}
```

5.启动并访问

访问 http://localhost:7001/ 可以看到eureka页面，说明单机部署完成

注册服务到Eureka

在cloud-provider-payment8001项目中添加Eureka客户端依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

修改yml文件

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-payment-service
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  #当前数据源操作类型
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cloud?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true
    username: root
    password: admin
#########################################
eureka:
  client:
    register-with-eureka: true #是否将自己注册进EurekaServer，默认为true
    fetch-registry: true #是否从EurekaServer中抓取已有注册信息，默认为true，单节点时无所谓
    service-url:
      defaultZone: http://localhost:7001/eureka #设置eurekaServer地址
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yango.springcloud.entities  #所有entity别名所在包
```

```java
@SpringBootApplication
@EnableEurekaClient	//启动Eureka client端
public class PaymentMain8001 {

    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8001.class, args);
    }
}
```

启动cloud-eureka-server7001、cloud-provider-payment8001并访问http://localhost:7001 发现cloud-payment-service服务已经注册

接着接入服务消费者

在cloud-customer-order80中添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

修改yml文件

```yml
server:
  port: 80
spring:
  application:
    name: cloud-order-service	#eurekaserver上注册的服务名
eureka:
  client:
    register-with-eureka: true #表示是否将自己注册进eurekaserver，默认为true
    fetch-registry: true #是否从eurekaserver抓取已有的注册信息，默认为true。单间点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    service-url:
      defaultZone: http://localhost:7001/eureka
```

修改主启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class OrderMain80 {

    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
}
```

启动后并访问http://localhost:7001 发现cloud-order-service服务已经注册

## 集群Eureka

服务注册：将服务信息注册进注册中心

服务发现：从注册中心上获取服务信息

实质：存key服务名，取value调用地址

1.先启动eureka注册中心

2.启动服务提供者payment支付服务

3.支付服务启动后会把自身信息(比如服务地址以别名方式注册进eureka)

4.消费者order服务在需要调用接口时，使用服务别名去注册中心获取实际的RPC远程调用地址

5.消费者获得调用地址后，底层实际是利用HttpClient技术实现远程调用

6.消费者获得服务地址后缓存在本地jvm内存中，默认每间隔30s更新一次服务调用地址

微服务RPC远程服务调用最核心的是：高可用，试想你的注册中心只有一个only one，他出故障了，那你的这个微服务环境将不可用，所以我们需要搭建Eureka注册中心集群，实现负载均衡+故障容错

eureka集权原理：互相注册，每个节点有当前集群中所有其他节点的相关信息

参考相同模式(cloud-eureka-server7001)新建cloud-eureka-server7002

修改本地hosts文件C:\Windows\System32\drivers\etc\hosts

127.0.0.1        localhost
127.0.0.1		eureka7001.com
127.0.0.1		eureka7002.com

127.0.0.1		eureka7003.com

修改cloud-eureka-server7001、cloud-eureka-server7002的yml文件

cloud-eureka-server7001

```yml
server:
  port: 7001
eureka:
  instance:
    hostname: eureka7001.com #Eureka服务端实例名称
  client:
    register-with-eureka: false #false表示不向注册中心注册自己
    fetch-registry: true #表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7002.com:7002/eureka
```

cloud-eureka-server7002

```yml
server:
  port: 7002
eureka:
  instance:
    hostname: eureka7002.com #Eureka服务端实例名称
  client:
    register-with-eureka: false #false表示不向注册中心注册自己
    fetch-registry: true #表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka7001.com:7001/eureka
```

主启动类

```java
@SpringBootApplication
@EnableEurekaServer     //表示当前服务就是Eureka的服务端注册中心
public class EurekaMain7002 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7002.class, args);
    }
}
```

接下来访问http://eureka7001.com:7001/ 和http://eureka7002.com:7002/ 能正常看到eureka管理端，并可以看到 DS Replicas 下配置的eureka7001.com、eureka7001.com两个服务名，则说明集群配置成功

接着改造cloud-customer-order80和cloud-provider-payment8001的yml使其加入eureka集群

```yml
server:
  port: 80
spring:
  application:
    name: cloud-order-service
eureka:
  client:
    register-with-eureka: true #表示是否将自己注册进eurekaserver，默认为true
    fetch-registry: true #是否从eurekaserver抓取已有的注册信息，默认为true。单间点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    service-url:
      defaultZone: http://eureka7001:7001/eureka,http://eureka7002:7002/eureka
```

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-payment-service
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  #当前数据源操作类型
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cloud?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true
    username: root
    password: admin

eureka:
  client:
    register-with-eureka: true #是否将自己注册进EurekaServer，默认为true
    fetch-registry: true #是否从EurekaServer中抓取已有注册信息，默认为true，单节点时无所谓
    service-url:
      defaultZone: http://eureka7001:7001/eureka,http://eureka7002:7002/eureka #设置eurekaServer地址
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yango.springcloud.entities  #所有entity别名所在包
```

然后先启动cloud-eureka-server7001和cloud-eureka-server7002，再启动cloud-provider-payment8001和cloud-customer-order80

接着访问http://localhost/consumer/payment/get/1看是否访问成功

搭建服务提供者集群

仿照cloud-provider-payment8001新建cloud-provider-payment8002，注意修改端口号和主启动类类名，其他代码均一致

由于提供者是集群，这就需要进行负载均衡

修改8001和8002的controller，新增获取端口号，并打印

```java
@Value("${server.port}")
private String SERVER_PORT;
```

此时访问

http://localhost:8001/payment/get/1 ，则会打印8001

http://localhost:8002/payment/get/1 ，则会打印8002

http://localhost/consumer/payment/get/1 ， 则会一直打印8001

查看代码发现在cloud-customer-order80中请求的服务端口号是写死的

private static final String PAYMENT_URL = "http://localhost:8001";

所以需要调整为 private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";  ，注意这里的CLOUD-PAYMENT-SERVICE即为服务名，即是我们在application.yml中配置的

```yml
spring:
  application:
    name: cloud-payment-service
```

也即eureka中服务列表展示的 **CLOUD-PAYMENT-SERVICE**

修改后重启，重新访问http://localhost/consumer/payment/get/1 ，此时服务报错

java.net.UnknownHostException: CLOUD-PAYMENT-SERVICE

此时需要开启RestTemplate的负载均衡功能，即在配置类中新增注解

```java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced	//开启RestTemplate的负载均衡功能
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
```

此时多次访问http://localhost/consumer/payment/get/1 ，发现RestTemplate默认的负载均衡策略是轮询

## actuator微服务信息完善(可选)

首先需要在pom文件中导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

![image-20210124211750877](https://gitee.com/img/20210124211809.png)

发现服务状态下含有主机名称，如果主机名称调整，则这里会同步变更

我们的目的是只暴露服务名称，不暴露主机名

修改cloud-provider-payment8001、cloud-provider-payment8002的pom文件

```yml
eureka:
  client:
    register-with-eureka: true #是否将自己注册进EurekaServer，默认为true
    fetch-registry: true #是否从EurekaServer中抓取已有注册信息，默认为true，单节点时无所谓
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka,http://eureka7001.com:7001/eureka #设置eurekaServer地址
  instance:
    instance-id: payment8002	#新增实例id
    
```

![image-20210124212915551](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210124212915551.png)

然后访问http://host.docker.internal:8001/actuator/health，发现状态为UP，说明启动成功

### 2.访问信息有IP信息提示

希望点击![image-20210124211959883](https://gitee.com/img/20210124212001.png)

后查看到相关的ip信息，修改yml文件

```yml
eureka:
  client:
    register-with-eureka: true #是否将自己注册进EurekaServer，默认为true
    fetch-registry: true #是否从EurekaServer中抓取已有注册信息，默认为true，单节点时无所谓
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka,http://eureka7001.com:7001/eureka #设置eurekaServer地址
  instance:
    instance-id: payment8001 #修改status下的名称
    prefer-ip-address: true #回显ip地址
```

此时点击

![image-20210124213347337](https://gitee.com/img/20210124213349.png)

后会跳转到对应ip地址上面，说明配置成功

| Endpoint ID    | Description                                                  |
| -------------- | ------------------------------------------------------------ |
| auditevents    | 显示应用暴露的审计事件 (比如认证进入、订单失败)              |
| info           | 显示应用的基本信息                                           |
| health         | 显示应用的健康状态                                           |
| metrics        | 显示应用多样的度量信息                                       |
| loggers        | 显示和修改配置的loggers                                      |
| logfile        | 返回log file中的内容(如果logging.file或者logging.path被设置) |
| httptrace      | 显示HTTP足迹，最近100个HTTP request/repsponse                |
| env            | 显示当前的环境特性                                           |
| flyway         | 显示数据库迁移路径的详细信息                                 |
| liquidbase     | 显示Liquibase 数据库迁移的纤细信息                           |
| shutdown       | 让你逐步关闭应用                                             |
| mappings       | 显示所有的@RequestMapping路径                                |
| scheduledtasks | 显示应用中的调度任务                                         |
| threaddump     | 执行一个线程dump                                             |
| heapdump       | 返回一个GZip压缩的JVM堆dump                                  |

配置跳转后的info信息，

首先在cloud-provider-payment8001项目的pom文件中添加如下配置

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <fork>true</fork>
                <addResources>true</addResources>
            </configuration>
        </plugin>
    </plugins>
    <resources>
        <resource><!--如果不配置次resource  yml中的"@project.artifactId@"无法取到对应的值-->
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>
```

接下来在yml中新增info信息，此功能可以用来描述某个服务模块的作用等等信息

```yml
info:
  app:
    name: cloud-payment-service-8001
  company:
    name: www.baidu.com
  build:
    artifactId: "@project.artifactId@"
    version: "@project.version@"
```

然后访问payment8001显示如下

![image-20210124221038805](https://gitee.com/img/20210124221040.png)



## 服务发现Discovery

对于注册进eureka中的微服务，可以通过服务发现来获得该服务的信息

在cloud-provider-payment8001中注入org.springframework.cloud.client.discovery.DiscoveryClient并新增方法

```java
@GetMapping("/payment/discovery")
public Object discovery() {
    List<String> services = discoveryClient.getServices();
    for (String service : services) {
        log.info("==element==" + service);
    }
    List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

    for (ServiceInstance instance : instances) {
        log.info("==instance==",instance.getHost(),instance.getInstanceId(),instance.getServiceId());
    }
    return discoveryClient;
}
```

然后在主启动类上添加 @EnableDiscoveryClient 表示启动服务发现

访问 http://localhost:8001/payment/discovery ，展示如下

**{**"services": **[**"cloud-payment-service","cloud-order-service"**]**,"order": 0}

Eureka自我保护

保护模式主要用于一组客户端和Eureka Server之间存在网络分区场景下的保护。一旦进入保护模式，Eureka Server将会尝试保护其服务注册表中的信息，不再删除服务注册表中的数据，也就是不会注销任何微服务，也就是说，某时刻一个微服务不可用了，Eureka不会立即清理，依旧会对该服务的信息进行保存。

是为了防止EurekaClient可以正常运营，但是与EurekaServer网络不通的情况下，EurekaServer不会立即将EurekaClient服务提出

什么是自我保护模式

默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务的心跳，EurekaServer将会注销该实例(默认90秒)。但是当网络分区故障发生(延时、卡顿、拥挤)时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了-因为微服务本身是健康的，此时不应该注销这个微服务。Eureka通过“自我保护模式”来解决这个问题-放EurekaServer节点在短时间内丢失过多客户端时(可能发生了网络分区故障)，那么这个节点就会进入自我保护模式。综上，自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务(江康德微服务和不健康的微服务都会保留)也不盲目注销任何健康的微服务。使用自我保护模式，可以让Eureka集群更加的健壮、稳定。

禁用自我保护机制

在cloud-eureka-server7001服务的yml文件中添加配置

```yml
server:
  port: 7001
eureka:
  instance:
    hostname: eureka7001.com #Eureka服务端实例名称
  client:
    register-with-eureka: false #false表示不向注册中心注册自己
    fetch-registry: true #表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
#      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
#      defaultZone: http://eureka7002.com:7002/eureka
      defaultZone: http://eureka7001.com:7001/eureka
  server:
    enable-self-preservation: false	#关闭eureka server自我保护机制，保证不可用服务及时剔除(默认是true)
    eviction-interval-timer-in-ms: 2000 #设置为2秒
```

此时访问 http://eureka7001.com:7001/ 出现如下提示，说明已经关闭自我保护机制

![image-20210127222047552](https://gitee.com/img/20210127222056.png)

接下来调整cloud-provider-payment8001配置文件

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-payment-service
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource  #当前数据源操作类型
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cloud?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true
    username: root
    password: admin

eureka:
  client:
    register-with-eureka: true #是否将自己注册进EurekaServer，默认为true
    fetch-registry: true #是否从EurekaServer中抓取已有注册信息，默认为true，单节点时无所谓
    service-url:
#      defaultZone: http://eureka7002.com:7002/eureka,http://eureka7001.com:7001/eureka #设置eurekaServer地址
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    instance-id: payment8001 #修改status下的名称
    prefer-ip-address: true #回显ip地址
#    eureka客户端向服务端发送心跳的时间间隔，单位为秒(默认30秒)
    lease-renewal-interval-in-seconds: 1
#    Eureka服务端在收到最后一次心跳后等待的时间上限，单位为秒(默认是90秒)，超时将剔除服务
    lease-expiration-duration-in-seconds: 2
info:
  app:
    name: cloud-payment-service-8001
  company:
    name: www.yglicai.com.cn
  build:
    artifactId: "@project.artifactId@"
    version: "@project.version@"

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.yango.springcloud.entities  #所有entity别名所在包
```

这时候我们启动cloud-provider-payment8001，发现eureka上注册成功，此时我们再关闭cloud-provider-payment8001，发现eureka在2秒后剔除了cloud-provider-payment8001服务



## zookeeper注册中心

### 服务提供者

新建项目cloud-provider-payment8004并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-provider-payment8004</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--SpringBoot整合Zookeeper客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <exclusions>
                <!--先排除自带的zookeeper3.5.3，以免版本不一致导致错误-->
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--添加zookeeper3.4.6版本-->
        <dependency><!--注意如果与zookeeper版本不一致可能会报错-->
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.9</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新增application.yml

```yml
server:
  port: 8004
spring:
  application:
    name: cloud-provider-payment #注册进zookeeper的服务名
  cloud:
    zookeeper:
      connect-string: 192.168.136.135:2181	#zookeeper注册中心地址
```

然后编写Controller

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {
    @Value("${server.port}")
    private String SERVER_PORT;
    @GetMapping("/payment/zk")
    public String paymentzk() {
        return "springcloud with zookeeper :" + SERVER_PORT + "\t" + UUID.randomUUID().toString();
    }
}
```

接着启动主启动类，然后使用zookeeper客户端登录查看发现服务正常注册

![image-20210131212623636](https://gitee.com/img/20210131212625.png)然后访问http://localhost:8004/payment/zk 页面打印

springcloud with zookeeper :8004 1fdcff9d-2ceb-4d9a-9ae0-61a114937bd7

当我们关闭8004的服务，发现zookeeper并没有马上清除服务，而是在一定的心跳时间之后才清除相关不可用服务，而当我们重新启动8004服务后，会发现zookeeper有重新创建了跟上次service id不一样的节点，所以zookeeper的服务节点是临时节点。即zookeeper关闭后节点即清除

### 服务消费者

新建cloud-customerzk-order80模块并修改pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-customerzk-order80</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--SpringBoot整合Zookeeper客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <exclusions>
                <!--先排除自带的zookeeper3.5.3-->
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--添加zookeeper3.4.6版本-->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.6</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新增yml配置文件

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-order #在zookeeper注册的服务名
  cloud:
    zookeeper:
      connect-string: 192.168.136.135:2181 #zookeeper注册中心地址
```

新增配置类，用于注入RestTemplate并实现负载

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

接着新增controller调用cloud-provider-payment8004提供的服务

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderZKController {
    public static final String INVOKE_URL="http://cloud-provider-payment";
    @Resource
    private RestTemplate restTemplate;
    @GetMapping("/payment/zk")
    public String paymentInfo () {
        String result = restTemplate.getForObject(INVOKE_URL + "/payment/zk", String.class);
        return "consumer调用provider=======" +  result;
    }
}
```

启动cloud-provider-payment8004、cloud-customerzk-order80。

然后访问http://localhost/consumer/payment/zk返回

consumer调用provider=======springcloud with zookeeper :8004 788f6f7f-3db7-4379-89d7-6e0fc3eccce4

说明调用成功，此时查看zookeeper中的服务发现服务提供者和消费者都已经注册成功

![image-20210131215307104](https://gitee.com/img/20210131215308.png)

## Consul注册中心

官网：https://www.consul.io/docs/intro，它是一套分布式的服务发现和配置管理系统，有HashCorp公司用Go语言开发，其提供了微服务系统中的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网格，总之Consul提供了一种完整的服务网格解决方案。

它具有很多优点。包括：基于raft协议，比较简洁；支持健康检查，同时支持HTTP和DNS协议，支持跨数据中心的WAN集群，提供图形界面，跨平台、支持Linux、Mac、Windos

Spring Cloud Consul具有如下特性:

服务发现、健康检查、KV存储、多数据中心、可视化Web界面

下载并解压Consul

运行 consul.exe agent -dev 后访问 http://localhost:8500/ui/dc1/services 可以看到consul的web页面

![image-20210131222721732](https://gitee.com/img/20210131222723.png)

### 服务提供者

新建 cloud-providerconsul-payment8006 模块并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-providerconsul-payment8006</artifactId>
    <dependencies>
        <!--SpringCloud consul-server-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建yml文件

```yml
server:
  port: 8006

spring:
  application:
    name: consul-provider-payment
    #consul注册中心地址
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
#        hostname: 127.0.0.1
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8006 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8006.class, args);
    }
}
```

新建controller

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
@RestController
@Slf4j
public class PaymentController {
    @Value("${server.port}")
    private String SERVER_PORT;
    @GetMapping("/payment/consul")
    public String paymentzk() {
        return "springcloud with consul :" + SERVER_PORT + "\t" + UUID.randomUUID().toString();
    }

}
```

启动 cloud-providerconsul-payment8006 模块并访问 http://localhost:8500/ui/dc1/services 发现已经多了一个服务 consul-provider-payment，即我们在配置文件中配置的服务名 接着访问我们的接口http://localhost:8006/payment/consul 能正常打印

### 服务消费者

新建 cloud-customerconsul-order80 模块并修改pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-customerconsul-order80</artifactId>
    <dependencies>
        <!--SpringCloud consul-server-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 80
spring:
  application:
    name: cloud-consumer-order
  cloud:
    consul:
      host: localhost
      discovery:
        service-name: ${spring.application.name}
      port: 8500
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class OrderConsulMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderConsulMain80.class, args);
    }
}
```

新建配置类，配置RestTemplate

```java
package com.yango.springcloud.config;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

新建controller

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderConsulController {
    public static final String INVOKE_URL="http://consul-provider-payment";
    @Resource
    private RestTemplate restTemplate;
    @GetMapping("/payment/consul")
    public String paymentInfo () {
        String result = restTemplate.getForObject(INVOKE_URL + "/payment/consul", String.class);
        return "consumer调用provider=======" +  result;
    }
}

```

启动 cloud-customerconsul-order80 并访问 http://localhost:8500/ui/dc1/services 可以看到我们的 cloud-consumer-order 消费者已经注册成功，然后访问 http://localhost/consumer/payment/consul 可以正常返回

|  组件名   | 语言 | CAP  | 服务健康检查 | 对外暴露接口 | SpringCloud集成 |
| :-------: | :--: | :--: | :----------: | :----------: | :-------------: |
|  Eureka   | Java |  AP  |   可配支持   |     HTTP     |     已集成      |
|  Consul   |  Go  |  CP  |     支持     |   HTTP/DNS   |     已集成      |
| Zookeeper | Java |  CP  |     支持     |    客户端    |     已集成      |

CAP：

Consistency：强一致性

Availability：可用性

Partition tolerance：分区容错性

最多只能同时较好的满足两个。

CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性，和分区容错性这三个需求，因此，根据CAP原理将NoSQL数据库分成了满足CA原则，满足CP原则和满足AP原则三大类：

AP-单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大

CP-满足一致性，分区容忍性的系统，通常性能不是特别高

AP-满足可用性，分区容忍性的系统，通常可能对一致性要求低一些

## Ribbon负载均衡服务调用

SpringCloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具，其已经进入维护模式。

简单的说，Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法和服务调用。Ribbon客户端组件提供一系列完善的配置项，如连接超时，重试等。简单的说，就是在配置文件中列出LoadBalancer(简称LB)后面所有的及其，Ribbon会自动的帮助你基于某种规则(如简单轮询，随机连接等)去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。

LB负载均衡(LoadBalance)是什么

简单的说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA(高可用)。

常见的负载均衡软件又Nginx、LVS硬件F5等。

Ribbon本地负载均衡VSNginx服务端负载均衡区别

Nginx是服务器负载均衡，客户端所有请求都会交给nginx，然后由nginx实现转发请求，即负载均衡是由服务端实现的。

Ribbon本地负载均衡，在调用微服务接口的时候，会在注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术。

Ribbon其实是一个软负载均衡的客户端组件，他可以和其他所需请求的客户端结合使用，和eureka结合只是其中一个实例。

之前写样例的时候并没有引入spring-cloud-starter-ribbon也可以使用ribbon，是因为我们引入了新版的spring-cloud-starter-netflix-eureka-client中已经引入了spring-cloud-starter-netflix-ribbon的依赖

![image-20210206221844875](https://gitee.com/img/20210206221945.png)

### 核心组件IRule

IRule：根据特定算法中从服务列表中选取一个要访问的服务

![image-20210206223924586](https://gitee.com/img/20210206223925.png)

RoundRobinRule：轮询

RandomRule：随机

RetryRule：先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务

WeightedResponseTimeRule：对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择

BestAvailableRule：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量小的服务

AvailabilityFilteringRule：先过滤掉故障实例，在选择并发较小的实例

ZoneAvoidanceRule：默认规则，复合判断server所在区域的性能和server的可用性选择服务器

### 替换默认负载规则

Ribbon的负载均衡配置类不能放在@ComponentScan所扫描的当前包下以及子包下，否则我们自定义的这个配置类就会被所有的Ribbon客户端所共享，达不到特殊化定制的目的

新建配置类，注意其不能在主启动类所扫描的包下

```java
package com.yango.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule {

    /**
     * 自定义随机负载
     */
    @Bean
    public IRule myRule() {
        return new RandomRule();
    }

}
```

在主启动类上新增注解

```java
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)
```

轮询的原理是，拿请求次数取模服务数量，然后作为集合下标取出提供服务的机器

手写负载均衡算法，注意注释掉原有注入RestTemplate注入的注解@LoadBalanced

```java
package com.yango.springcloud.lb;
import org.springframework.cloud.client.ServiceInstance;
import java.util.List;
/**
 * 定义服务集群实例接口
 */
public interface ILoadBalancer {
    ServiceInstance instance (List<ServiceInstance> serviceInstances);
}
```

```java
package com.yango.springcloud.lb.impl;
import com.yango.springcloud.lb.ILoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 手写轮询负载均衡算法
*/
@Component
public class MyLB implements ILoadBalancer {
    private AtomicInteger nextServerCyclicCounter;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    //获取访问次数
    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("======第几次访问 next=======" + next);
        return next;
    }
    @Override// 获取提供服务的实例
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {
        //获取第几次访问与服务集群刷量的取模，得到实际调用服务的下标
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
```

```java
package com.yango.springcloud.controller;

import com.yango.springcloud.entities.CommonResult;
import com.yango.springcloud.entities.Payment;
import com.yango.springcloud.lb.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.rmi.server.LoaderHandler;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * 使用自定义的负载均衡算法，注意注释掉配置类中注入RestTemplate时使用的@LoadBalanced注解
 */
@Slf4j
@RestController
@RequestMapping("/consumer")
public class OrderController {
    private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    @Autowired
    private RestTemplate template;
    @Resource
    private ILoadBalancer loadBalancer;
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 自定义负载均衡算法
     */
    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances == null || instances.size() <= 0) {
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instance(instances);
        URI uri = serviceInstance.getUri();
        return template.getForObject(uri + "/payment/lb", String.class);
    }
}
```

## OpenFeign

feign是一个声明式的WebService客户端，使用Feign也可以支持可插拔式的编码器和解码器。SpringCloud对Feign进行了封装，使其支持了SpringMVC标准注解和HttpMessageConverts。Feign可以与Eureka和Ribbon组合使用，以支持负载均衡。

他是一个声明式的Web服务客户端，让编写服务客户端变的非常容易，只需创建一个接口并在接口上添加注解即可

Feign旨在使编写Http客户端变的更容易。前面在使用Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模板化的调用方法。但是在实际开发中，由于对服务依赖的调用可能不止一处，往往一个接口会被多出调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以，Feign在此基础上做了进一步的封装，由他来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下，我们只需创建一个接口并使用注解的方式来配置他(以前是Dao接口上面标注Mapper注解，现在是一个微服务接口上标注一个Feign注解即可)，即可完成对服务提供提供方的接口绑定，简化了使用SpringcloudRibbon时，自动封装服务调用客户端的开发量。

Feign集成了Ribbon，利用Ribbon维护了Payment的服务列表信息，并且通过轮询实现了客户端的负载均衡。而与Ribbon不同的是，通过frign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用。

### 区别

| Feign(停更)                                                  | OpenFeign                                                    |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| Feign是Springcloud组件中一个轻量级RESTful的HTTP服务客户端，Feign内部内置了Ribbon，用来做客户端负载均衡，去调用服务注册中心的服务，Feign的适用方式是：适用Feign的注解定义接口，调用这个接口，就可以调用服务注册中心的服务 | OpenFeign是SpringCloud在Feign的基础上支持了SpringMVC的注解，如@RequestMapping等等。OpenFeign的@FeignClient可以解析SpringMVC的@RequestMapping注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务 |
| <groupId>org.springframework.cloud</groupId>     <artifactId>spring-cloud-starter-feign</artifactId> | <groupId>org.springframework.cloud</groupId>     <artifactId>spring-cloud-starter-openfeign</artifactId> |

openFeign默认支持Ribbon，自带负载均衡配置项，openfeign底层就是ribbon消费者一般默认等待1秒，超时会报错

### 案例

新建cloud-consumer-feign-order80 模块，并修改pom文件

```xml
<dependencies>
    <!--openfeign 新增OpenFeign依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!--eureka client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--监控-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.yango.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

新建配置文件

```yml
server:
  port: 80
# 注册中心地址
eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients//激活并开启feign
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class, args);
    }
}
```

新建feign代理接口

```java
import com.yango.springcloud.entities.CommonResult;
import com.yango.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Component
@FeignClient("CLOUD-PAYMENT-SERVICE")//注册中心里的服务名
public interface PaymentFeignService {
    @GetMapping("/payment/get/{id}")//该服务下具体接口的请求地址
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

}
```

创建controller实现调用

```java
import com.yango.springcloud.entities.CommonResult;
import com.yango.springcloud.entities.Payment;
import com.yango.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;//注入刚创建的feign代理接口，并调用具体方法
    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }
}
```

然后分别启动cloud-eureka-server7001、cloud-eureka-server7002、cloud-provider-payment8001、cloud-provider-payment8002、cloud-consumer-feign-order80并多次请求 http://localhost/consumer/payment/get/1 发现正常请求并实现了轮询的负载均衡。

默认Feign客户端只等待1秒，但是服务端处理需要超过1秒，导致Feign客户端不想等待了，直接返回错误。为了避免这样的情况，有时候我们需要设置Feign客户端的超时控制

在 cloud-provider-payment8001 提供者中新增接口

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
    @Value("${server.port}")
    private String SERVER_PORT;
    /**
     *测试feign超时
     */
    @GetMapping("/feign/timeout")
    public String paymentFeignTimeout() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return SERVER_PORT;
    }
}
```

在Feign接口中新增接口

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
@Component
@FeignClient("CLOUD-PAYMENT-SERVICE")
public interface PaymentFeignService {
    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout();
}
```

然后访问 http://localhost:8001/payment/feign/timeout 发现等待3秒页面正常返回，接着请求消费者接口 http://localhost/consumer/payment/feign/timeout

发现超过1秒后直接报错 Read timed out

接着我们设置feign的超时时间，在 **cloud-consumer-feign-order80** 的配置文件中添加如下配置

```yml
# 设置feign客户端的超时时间(openfeign默认支持ribbon)
ribbon:
  ReadTimeout: 5000 # 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间
  ConnectTimeout: 5000 # 指的是建立连接后从服务器读取到可用资源所用的时间
```

然后重新访问 http://localhost/consumer/payment/feign/timeout 等待3秒后服务正常返回

Feign提供了日志打印功能，我们可以通过配置来调整日志级别，从而了解Feign中Http请求的细节，说白了就是付Feign接口的调用情况进行监控和输出。其支持的日志级别如下：

NONE：默认的，不显示任何日志。

BASIC：仅记录请求方法、URL、响应状态码及执行时间。

HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息。

FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据。

配置feign的日志级别，在 cloud-consumer-feign-order80 模块中新增配置类

```java
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

在 cloud-consumer-feign-order80 配置文件中新增配置

```yml
logging:
  level:
    #feign日志以什么级别监控哪个接口
    com.yango.springcloud.service.PaymentFeignService: debug
```

然后重新请求 http://localhost/consumer/payment/get/1 地址，查看控制台，可以看到已经打印了请求的详细信息

## Hystrix断路器(停更)

复杂的分布式体系结构中的应用程序有数十个依赖关系，每个依赖关系在某些时候将不可避免的失败，这样如果一个请求涉及多个服务，其中一个服务无法响应则可能会导致最初的调用方出现系统崩溃。

### 服务雪崩

多个微服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其他的微服务，这就是所谓的扇出。如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，这就是所谓的"雪崩效应"。

对于高流量的应用来说，单一的后端依赖可能会导致所有服务器上的所有资源都在几秒内饱和。比失败更糟糕的是，这些应用程序还可能导致服务之间的延迟增加，备份队列，线程和其他系统资源紧张，导致整个系统发生更多的级联故障。这些都表示需要对故障和延迟进行隔离和管理，以便单个依赖关系的失败，不能取消整个应用程序或系统。

所以，通常你发现一个模块下的某个实例失败后，这时候这个模块依然还会接接收流量，然后这个有问题的模块还调用了其他的模块，这样就会发生级联故障，或者交雪崩。

### Hystrix简介

Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出现问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性。

“断路器”本身是一种开关装置，当某个服务单元发生故障后，通过断路器的故障监控(类似熔断保险丝)，向调用方返回一个符合预期的、可处理的备选响应(FallBack)，而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。

其主要作用是，服务降级、服务熔断、接近实时的监控、服务限流、服务隔离等等。

#### 服务降级(fallback)

服务器忙，请稍后再试，不让客户端等待并立即返回一个友好提示，fallback

降级场景：程序运行异常、超时、服务熔断出发服务降级、线程池/信号量打满也会导致服务降级。

#### 服务熔断(break)

类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示

服务的降级->进而熔断->回复调用链路

#### 服务限流(flowlimit)

秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行

### 案例

首先调整 cloud-eureka-server7001 注册中心为单机版，修改其配置文件如下即可

```yml
server:
  port: 7001
eureka:
  instance:
    hostname: eureka7001.com #Eureka服务端实例名称
  client:
    register-with-eureka: false #false表示不向注册中心注册自己
    fetch-registry: true #表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
    service-url:
      #设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
#      defaultZone: http://eureka7002.com:7002/eureka
#      defaultZone: http://eureka7001.com:7001/eureka
  server:
    enable-self-preservation: false    #关闭eureka server自我保护机制，保证不可用服务及时剔除(默认是true)
    eviction-interval-timer-in-ms: 2000 #设置为2秒
```

然后新建 cloud-provider-hystrix-payment8001 模块并修改pom文件

```xml
<dependencies>
    <!--hystrix断路器依赖-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    </dependency>
    <!--eureka client-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <dependency>
        <groupId>com.yango.springcloud</groupId>
        <artifactId>cloud-api-commons</artifactId>
        <version>${project.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--监控-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!--热部署-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

新建配置文件application.yml 

```yml
server:
  port: 8001
spring:
  application:
    name: cloud-provider-hystrix-payment

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
    register-with-eureka: true
    fetch-registry: true
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class PaymentHytrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHytrixMain8001.class, args);
    }
}
```

新建controller

```java
import com.yango.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String SERVER_PORT;
    @GetMapping("hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_OK(id);
        log.info("#######paymentInfo_OK#result#########:" + result);
        return result;
    }
    @GetMapping("hytrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_TimeOut(id);
        log.info("#######paymentInfo_TimeOut#result#########:" + result);
        return result;
    }
}
```

新建service接口及其实现类

```java
public interface PaymentService {
    public String paymentInfo_OK(Integer id);
    public String paymentInfo_TimeOut(Integer id);
}
```

```java
import com.yango.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
@Service
public class PaymentServiceImpl implements PaymentService {
    /**
     * 正常访问的方法
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_OK,id:" + id + "  正常";
    }
    /**
     * 时间延迟3秒的方法
     */
    public String paymentInfo_TimeOut(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_TimeOut,id:" + id + "  超时:3s";
    }

}
```

然后启动cloud-eureka-server7001、cloud-provider-hystrix-payment8001并访问 http://localhost:8001/payment/hytrix/ok/1 、http://localhost:8001/payment/hytrix/timeout/1 可以正常访问。以上均为正确接口访问，并且ok的地址毫秒级响应

接着实现，正确->错误->降级熔断->恢复

接下来使用JMeter模拟20000个线程每秒调用一次 http://localhost:8001/payment/hytrix/timeout/1 地址，然后访问 http://localhost:8001/payment/hytrix/ok/1 地址发现响应明显变慢(秒级响应)，已经被拖累。此时如果外部的消费者80也来访问，那消费者只能干等，最终导致消费端80部门已，服务端8001直接被拖死。原因是tomcat的默认的工作线程数被打满了，没有多余的线程来分解压力和处理。

hytrix在服务端和消费端都可以添加，但是一般是用在消费端

新建hytrix消费端模块 cloud-consumer-feign-hystrix-order80 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>    <dependencies>
        <!--hystrix断路器-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!--openfeign-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!--eureka client-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--监控-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

新建配置文件 application.yml 

```java
server:
  port: 80
eureka:
  client:
    fetch-registry: true
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/ #入驻地址 不集群
```

主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients //启用Feign
public class OrderHytrixMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHytrixMain80.class, args);
    }
}
```

新建Feign接口

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT")//注册中心中的服务名
public interface PaymentHytrixService {
    @GetMapping("/payment/hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id);
    @GetMapping("/payment/hytrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id);
}
```

新建controller

```java
import com.yango.springcloud.service.PaymentHytrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderHytrixController {
    @Resource
    private PaymentHytrixService paymentHytrixService;
    @GetMapping("/payment/hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentHytrixService.paymentInfo_OK(id);
        return result;
    }
    @GetMapping("/payment/hytrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        String result = paymentHytrixService.paymentInfo_TimeOut(id);
        return result;
    }
}
```

然后启动cloud-eureka-server7001、cloud-provider-hystrix-payment8001、cloud-consumer-feign-hystrix-order80服务并访问

http://localhost/consumer/payment/hytrix/ok/1 正常访问，毫秒级响应，然后我们继续压测 http://localhost:8001/payment/hytrix/timeout/1 接口，再次访问 http://localhost/consumer/payment/hytrix/ok/1 发现其已经变慢，秒级响应，原因是8001同一层次的其他接口服务被困死，因为tomcat线程池里面的工作线程已经被挤占完毕，此时80调用8001，客户端访问响应缓慢。这时候就需要引入降级/容错/限流等技术。

两个方面，1.超时导致的服务提供变慢，不能在等待；2.出错(宕机或程序运行出错)时，要有兜底返回，而不是返回错误页面等信息。3.调用者自己出故障或者自我要求(调用者等待时间小于服务提供的时间)。综上都需要进行降级处理

### 服务降级

#### 一、服务端

1.降级配置：@HytrixCommand

2.8001自身调整：设置自身调用超时时间的峰值，峰值内可以正常运行，超过了需要有兜底的方法处理，做服务降级fallback

调整8001的paymentInfo_TimeOut接口

```java
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yango.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
@Service
public class PaymentServiceImpl implements PaymentService {
    /**
     * 正常访问的方法
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_OK,id:" + id + "  正常";
    }
    /**
     * fallbackMethod设置这个方法超时、报错等时的兜底方法，commandProperties设置当前接口自身执行的超时时间
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id) {
        //int age = 1/0;
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_TimeOut,id:" + id + "  超时:5s";
    }
    public String paymentInfo_TimeOutHandler () {
        return "线程池：" + Thread.currentThread().getName() + "id:" + id + ",服务繁忙，请稍后重试";
    }
}
```

在8001主启动类上新增 @EnableCircuitBreaker//启动hytrix断路器 注解

启动后访问 http://localhost:8001/payment/hytrix/timeout/1 页面展示出"线程池：HystrixTimer-1id:1,服务繁忙，请稍后重试",说明hytrix配置已经生效，并且启动了单独的线程池处理。说明当程序执行时间过长或者报错时会调用配置好的fallback方法

#### 二、消费端

消费端也可以设置自身的等待时间，这样就可以更好的保护自己，而不是依赖于服务端的超时时间。注意，我们自己配置过的热部署方式对java代码的改动明显，但对@HytrixCommand内属性的修改，建议重启微服务

修改 cloud-consumer-feign-hystrix-order80 配置文件

```yml
server:
  port: 80
eureka:
  client:
    fetch-registry: true
    register-with-eureka: false
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/ #入驻地址 不集群
#启用hytrix断路器
feign:
  hystrix:
    enabled: true
```

主启动类新增启用hystrix注解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
@EnableHystrix//启用hystrix
public class OrderHytrixMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHytrixMain80.class, args);
    }
}
```

修改相关接口

```java
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yango.springcloud.service.PaymentHytrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderHytrixController {
    @Resource
    private PaymentHytrixService paymentHytrixService;
    @GetMapping("/payment/hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentHytrixService.paymentInfo_OK(id);
        return result;
    }
    @GetMapping("/payment/hytrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        //int age = 1/0;
        String result = paymentHytrixService.paymentInfo_TimeOut(id);
        return result;
    }
    public String paymentInfo_TimeOutHandler (Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "id:" + id + ",消费者80繁忙，稍后重试";
    }
}
```

启动 cloud-eureka-server7001、cloud-provider-hystrix-payment8001、cloud-consumer-feign-hystrix-order80后访问 http://localhost/consumer/payment/hytrix/timeout/111 发现返回消费端降级兜底方法，可以发现如果服务端接口执行时间过长(超过消费端的超时时间)，会执行消费端的超时或报错的兜底方法。

#### 目前问题

1.每个业务方法对应一个兜底方法，代码膨胀。

2.每个业务方法都需要添加相关注解，重复性高。

3.统一的错误返回方法与自定义的接口方法放在一起，耦合性高。

解决：@DefaultProperties(defaultFallback="") 注解统一处理，此时只需要在对应方法上添加@HystrixCommand注解即可。

修改消费者

```java
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yango.springcloud.service.PaymentHytrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/consumer")
@DefaultProperties(defaultFallback = "payment_global_fallback_method")//定义全局异常/超时处理方法
public class OrderHytrixController {
    @Resource
    private PaymentHytrixService paymentHytrixService;
    @GetMapping("/payment/hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentHytrixService.paymentInfo_OK(id);
        return result;
    }
    @GetMapping("/payment/hytrix/timeout/{id}")
//    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
//    })
    @HystrixCommand
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        int age = 1/0;
        String result = paymentHytrixService.paymentInfo_TimeOut(id);
        return result;
    }
    /**
     * paymentInfo_TimeOut方法报错或者超时时的回调方法
     */
    public String paymentInfo_TimeOutHandler (@PathVariable("id") Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "id:" + id + ",消费者80繁忙，稍后重试";
    }
    /**
     * 全局的超时或者报错的回调方法
     */
    public String payment_global_fallback_method() {
        return "线程池：" + Thread.currentThread().getName() + " 全局异常处理，请稍后重试";
    }
}
```

重启后访问，http://localhost/consumer/payment/hytrix/timeout/111 执行了全局的回调方法。

#### 解耦

将业务逻辑与回调分开。服务降级，客户端去调用服务端，碰上服务端宕机或关闭

修改消费端的feign接口

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//PaymentFallbackService是PaymentHytrixService的实现类
@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
public interface PaymentHytrixService {
    @GetMapping("/payment/hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id);
    @GetMapping("/payment/hytrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id);
}
```

新增回调类

```java
import org.springframework.stereotype.Component;
@Component
public class PaymentFallbackService implements PaymentHytrixService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return "############PaymentFallbackService-paymentInfo_OK fall back";
    }
    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "############PaymentFallbackService-paymentInfo_TimeOut fall back";
    }
}
```

重启cloud-consumer-feign-hystrix-order80 后访问http://localhost/consumer/payment/hytrix/ok/111，可以看到服务正常，这是停掉 服务端 cloud-provider-hystrix-payment8001 后重新访问，发现执行了回调类中对应的方法。

### 服务熔断

熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。当检测到该节点微服务调用响应正常后，恢复调用链路。

在springcloud框架里熔断机制通过Hystrix实现，Hystrix会监控微服务间调用的情况，当失败的调用达到一定阈值，缺省是5s20次调用失败，就会启动熔断机制，熔断机制的注解是@HystrixCommand。

消费端配置熔断

修改服务service接口

```java
import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yango.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.concurrent.TimeUnit;
@Service
public class PaymentServiceImpl implements PaymentService {
    /**
     * 正常访问的方法
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_OK,id:" + id + "  正常";
    }
    /**
     * fallbackMethod设置这个方法超时、报错等时的兜底方法，commandProperties设置当前接口自身执行的超时时间
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public String paymentInfo_TimeOut(Integer id) {
//        int age = 1/0;
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池：" + Thread.currentThread().getName() + "  paymentInfo_TimeOut,id:" + id + "  超时:3s";
    }

    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池：" + Thread.currentThread().getName() + "id:" + id + ",服务繁忙，请稍后重试";
    }
    /************下面是服务熔断**************/
    /**
     * 在10秒窗口期中10次请求有6次是请求失败的,断路器将起作用
     *
     * @param id
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),// 时间窗口期/时间范围
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")// 失败率达到多少后跳闸
    }
    )
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("*****id不能是负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功,流水号:" + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数,请稍后重试,o(╥﹏╥)o id:" + id;
    }
}
```

修改接口controller

```java
import com.yango.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String SERVER_PORT;
    @GetMapping("hytrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_OK(id);
        log.info("#######paymentInfo_OK#result#########:" + result);
        return result;
    }
    @GetMapping("hytrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        String result = paymentService.paymentInfo_TimeOut(id);
        log.info("#######paymentInfo_TimeOut#result#########:" + result);
        return result;
    }
    /**********************下面是服务熔断****************************/
    @GetMapping("")
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        String result = paymentService.paymentCircuitBreaker(id);
        log.info("--------result---------" + result);
        return result;
    }
}
```

启动后访问 http://localhost:8001/payment/circuit/111 能够正常返回，调用 http://localhost:8001/payment/circuit/-1 返回 "id 不能负数,请稍后重试,o(╥﹏╥)o id:-1" 说明方法执行异常时服务进行了熔断处理，调用了回调的函数。如果这个时候持续调用id为负数的连接数次，然后再调用正确的地址同样也会进行熔断处理，直至多次调用正确的地址后恢复正常。

熔断类型

1.熔断打开：请求不再进行调用当前服务，内部设置时钟一般为MTTR(平均故障处理时间)，当打开时长达到所设计时钟则进入半熔断状态

2.熔断关闭：熔断关闭不会对服务进行熔断

3.熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则，则认为当前服务恢复正常，关闭熔断

![img](https://gitee.com/img/20210214161620.png)

涉及到断路器的三个重要参数：快照时间窗，请求总数阈值，错误百分比阈值

1.快照时间窗：断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒

2.请求总数阈值：在快照时间窗内，必须满足请求总数阈值才有资格熔断，默认20，意味着在10秒内，如果该hystrix命令的调用次数不足20次，即使所有的请求都超时或其他原因失败，断路器都不会打开。

3.错误百分比阈值：当请求总数在快照时间窗内超过了阈值，比如发生了30次调用，如果在这30次调用中，有15次发生了超时异常，也就超过50%的错误百分比，在默认设定50%阈值的情况下，这时候就会将断路器打开。

断路器开启关闭条件

1.当满足一定阈值的时候(默认10s内超过20个请求次数)

2.当失败率达到一定的时候(默认10s内超过50%的请求失败)

3.达到以上阈值，断路器将会开启

4.当开启的时候，所有请求都不会进行转发

5.一段时间后，(默认5s),这个时候断路器是半开状态，会让其中一个请求进行转发。如果成功，断路器会关闭，若失败，继续开启，重复4、5。

断路器打开之后，再有请求调用的时候，将不会调用主逻辑，而是直接调用降级fallback，通过断路器，实现了自动地发现错误并将降级逻辑切换为主逻辑，减少响应延迟的效果。

原来的主逻辑如何恢复？对于这一问题，hystrix也为我们实现了自动恢复功能，当断路器打开，对主逻辑进行熔断后，hystrix会启动一个休眠时间窗，在这个时间窗内，降级逻辑是临时的成为主逻辑，当休眠时间窗到期，断路器进入半开状态，释放一次请求到原来的主逻辑上，如果此次请求正常返回，那么断路器将继续闭合，主逻辑恢复，如果这次请求依然有问题，断路器进入打开状态，休眠时间窗重新计时。

### 服务监控hystrixDashboard

除了隔离依赖服务的调用以外，Hystrix还提供了准实时的调用监控(Hystrix Dashboard)，Hystrix会持续地记录所有通过Hystrix发起的请求的执行信息，并以统计报表和图表的形式展示给用户，包括美妙执行多少请求，多少成功多少失败等等，Netflix通过hystrix-metrics-event-stream项目实现了对以上指标的监控，SpringCloud也提供了Hystrix Dashboard的整合，对监控内容转化成可视化界面。

#### 环境搭建

新建 cloud-consumer-hystrix-dashboard9001 模块并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-consumer-hystrix-dashboard9001</artifactId>
    <dependencies>
        <!--hystrix dashboard 图形化界面依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!--web-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--监控-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--热部署-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件application.yml

```yml
server:
  port: 9001
```

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
@SpringBootApplication
@EnableHystrixDashboard //启用Hystrix Dashboard
public class HystrixDashboardMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardMain9001.class, args);
    }
}
```

在需要监控的模块中添加如下依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

启动 cloud-consumer-hystrix-dashboard9001 并访问 http://localhost:9001/hystrix 出现图形化界面即为成功。

实现 9001 监控 cloud-provider-hystrix-payment8001 在8001主启动类中添加配置

```java
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker//启动hytrix断路器
public class PaymentHytrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHytrixMain8001.class, args);
    }
    /**
     * 此配置是为了服务监控而配置，与服务容错本身无关，springCloud 升级之后的坑
     * ServletRegistrationBean因为springboot的默认路径不是/hystrix.stream
     * 只要在自己的项目中配置上下面的servlet即可
     */
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
```

接着访问 http://localhost:9001/hystrix 并将 http://localhost:8001/hystrix.stream 地址填入，然后点击Monitor Stream进入监控页面，之后可以访问  http://localhost:8001/payment/circuit/1 和http://localhost:8001/payment/circuit/-1 观察图形变化

## Gateway新一代网关

Cloud全家桶重有个很重要的组件就是网关，在1.x版本中都是采用了Zuul网关，但在2.x版本中，zuul的升级一直跳票，SpringCloud最后自己研发了一个网关替代Zuul，那就是SpringCloud Gateway，一句话：gateway是zuul1.x版的替代。Gateway是在Spring生态系统之上构建的API网关服务，基于Spring5，Spring Boot2和Project Reactor等技术。Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如：熔断、限流、重试等等

SpringCLoud Gateway是SpringCloud的一个全新项目，基于Spring5.0+SpringBoot2.0和Project Reactor等技术开发的网关，他旨在为微服务架构提供一种简单有效的统一的API路由管理方式。SpringCloud Gateway作为SpringCloud生态系统中的网关，目标是替代Zuul，在SpringCloud2.0以上版本中，没有对新版本的Zuul2.0以上最新高新能版本进行集成，仍然还是使用Zuul1.x非Reator模式的老版本，而为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reator模式通信框架Netty。SpringCloud Gateway的目标是提供统一路由方式切基于Filter链的方式提供了网关基本的功能，例如:安全，监控/指标和限流。

网关的主要功能：反向代理、鉴权、流量控制、熔断、日志监控...

为什么选择Gateway？一方面因为Zuul1.x已经进入了维护阶段，而且Gateway是SpringCLoud团队研发的，是亲儿子产品，值得信赖，而且很多功能Zuul都没有，用起来也非常的简单便捷。Gateway是基于异步非阻塞模型上进行开发的，性能方面不需要担心。虽然Netflix早就发布了最新的Zuul2.x，但SpringCloud貌似没有整合计划。而且Netflix相关组件都宣布进入维护期，不止前景如何。多方面综合考虑Gateway是很理想的网关选择。

SpringCLoud Gateway有如下特点

1.基于Spring Framework5，Project Reator和Spring Boot2.0进行构建

2.动态路由：能够匹配任何请求属性

3.可以对路由指定Predicate(断言)和Filter(过滤器)

4.集成对Hystrix的断路器功能

5.集成Spring Cloud服务发现功能

6.易于编写的Predicate(断言)和Filter(过滤器)

7.请求流量功能

8.支持路径重写

SpringCloud Gateway与Zuul区别

在SpringFinchley正式版之前，Spring Cloud推荐的网关是Netflix提供的Zuul

1.Zuul1.x，是一个基于阻塞I/O的API Gateway

2.Zuul1.x基于Servlet2.3使用阻塞架构，它不支持任何长连接(如WebSocket)Zuul的设计模式和Nginx较像，每次I/O操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成，但是差别是Nginx用C++实现，Zuul用Java实现，而JVM本身会有第一次加载较慢的情况，是的Zuul的性能相对较差。

3.Zuul2.x理念更先进，想基于Netty非阻塞和支持长连接，但SpringCloud目前还没有整合。Zuul2.x的性能较Zuul1.x有较大提升，在性能方面，根据官方提供的基准测试，SpringCloud Gateway的RPS(每秒请求数)是Zuul的1.6倍。

4.SpringCloud Gateway简历在Spring Freamwotk5、ProjectReator和Spring Boot2之上，使用非阻塞API。

5.SpringCloud Gateway还支持WebSocket，并且与Spring紧密集成，拥有更好的开发体验。

SpringCloud中所集成Zuul版本，采用Tomcat容器，使用的是传统的Servlet IO处理模型。这样就会有Servlet的生命周期

其缺点：Servlet是一个简单的网络IO模型，当请求进入Servlet container时，Servlet container就会为其绑定一个线程，在并发不高的场景下这种模型是适用的。但是一旦高并发(用jmeter压)，线程数就会上涨，而线程资源代价是昂贵的(上下文切换，内存消耗大)严重影响请求的处理时间。在一些简单的业务场景下，不希望为每个request分配一个线程，只需要一个线程或几个线程就能应对极大地并发的请求，这种业务场景下Servlet模型就没有优势，所以Zuul1.x是基于Servlet之上的一个阻塞处理模型，即Spring实现了处理所有request请求的一个Servlet(DispatcherServlet)并由该Servlet阻塞式处理。所以Zuul就无法摆脱Servlet模型的弊端。

传统的Web框架，比如说struts2，SpringMVC等都是基于ServletAPI与Servlet容器基础之上运行的。但是在Servlet3.1之后有了异步非阻塞的支持，而WebFlux是一个典型非阻塞异步的框架，它的核心是基于Reator的相关API实现的。相对于传统的web框架来说，他可以运行在注入Netty，Undertow及支持Servlet3.1的容器上，非阻塞式+函数式编程(Spring5中必须使用java8)。Spring WebFlux是Spring5.0引入的新的响应式框架，区别于SpringMVC，他不需要依赖ServletAPI，他是完全异步非阻塞的，并且基于Reator来实现响应式流规范。

### 三大核心概念

路由(Route)：路由是构建网关的基本模块，它由目标URI，一系列的断言和过滤器组成，如果断言为true则匹配该路由

断言(Predicate)：开发人员可以匹配请求中所有的内容(例如请求头或请求参数)，如果请求与断言相匹配则进行路由

过滤(Filter)：指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路有前或者之后对请求进行修改

客户端向SpringCloudGateway发送请求，然后GatewayHandlerMapping中找到与请求匹配的路由，将其发送到Gateway WebHandler。Handler再通过指定的过滤连来将请求发送到我们实际的服务执行业务逻辑，然后返回。过滤器之间用虚线分开是因为过滤器可能会发送代理请求钱("pre")或之后("post")执行业务逻辑。Filter在"pre"类型的过滤器可以参数校验，权限校验，流量控制，日志输出，协议转换等，在"post"类型的过滤器中可以做响应内容，响应头的修改，日志的输出，流量监控等有着非常重要的作用。其核心逻辑就是路由转发+执行过滤器连。

### 案例

#### 一、yml配置

新建模块 cloud-gateway-gateway9527 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-gateway-gateway9527</artifactId>
    <dependencies>
<!--        引入Springcloud Gateway网关依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!--gateway无需web和actuator-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.yango.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 9527
eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka

spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      routes:
        - id: payment_routh #路由的ID，没有固定规则，但要求唯一，建议配合服务名
          uri: http://localhost:8001  #配置匹配后提供服务的路由地址
          predicates:
            - Path=/payment/get/**  #断言，路径相匹配的进行路由

        - id: payment_routh2
          uri: http://localhost:8001
          predicates:
            - Path=/payment/lb/**
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class GateWayMain9527 {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMain9527.class, args);
    }
}
```

启动 cloud-eureka-server7001、cloud-provider-payment8001、cloud-gateway-gateway9527。先访问

http://localhost:8001/payment/lb 能够正常返回，接着走网关访问  http://localhost:9527/payment/lb 也能正常返回。

#### 二、硬编码配置

新建配置类

```java
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GateWayConfig {
    /**
     * 配置了一个id为path_route_yango的路由规则，当访问http://localhost:9527/guonei 时会自动转发到地址http://news.baidu.com/guonei
     */
    @Bean
    public RouteLocator contomRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        RouteLocator routeYango = routes.route("path_route_yango", r -> r.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        return routeYango;
    }
}
```

然后重启 cloud-gateway-gateway9527 服务后访问 http://localhost:9527/guonei 可以正常访问百度新闻。

默认情况下GateWay会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由的功能。

修改 cloud-gateway-gateway9527 模块的配置文件

```yml
server:
  port: 9527
eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka

spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能，利用微服务进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则，但要求唯一，建议配合服务名
#          uri: http://localhost:8001  #配置匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/get/**  #断言，路径相匹配的进行路由

        - id: payment_routh2
#          uri: http://localhost:8001
          uri: lb://cloud-payment-service #注册中心中的服务名称
          predicates:
            - Path=/payment/lb/**
```

重启后访问 http://localhost:9527/payment/lb 可以看到已经交替执行对应的服务，完成了从注册中心获取服务并进行负载均衡请求。

SpringCloud Gateway将路由匹配作为Spring WebFlux HandlerMapping基础架构的一部分。SpringCloud Gateway包括许多内置的Route Predicate工厂。所有这些Predicate都与HTTP请求的不同属性相匹配。多个Route Predicate工厂可以进行组合。Spring Cloud Gateway创建Route对象时，使用RoutePredicateFactory创建爱你Predicate对象，Predicate对象可以赋值给Route。SpringCloud Gateway包含许多内置的Route Predicate Factories。所有这些谓词都匹配HTTP请求的不同属性。多种谓词工厂可以组合，并通过逻辑and。并且其predicate可以匹配在什么时间之前、之后、之间路由才能起作用。相关配置如下

```yml
server:
  port: 9527
eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka

spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能，利用微服务进行路由
      routes:
        - id: payment_routh #路由的ID，没有固定规则，但要求唯一，建议配合服务名
#          uri: http://localhost:8001  #配置匹配后提供服务的路由地址
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/get/**  #断言，路径相匹配的进行路由

        - id: payment_routh2
#          uri: http://localhost:8001
          uri: lb://cloud-payment-service #注册中心中的服务名称
          predicates:
            - Path=/payment/lb/**
            - After=2021-02-15T20:28:58.064+08:00[Asia/Shanghai] #在这个时间后这个predicate才生效，否则访问报错404
            - Cookie=username,yango #匹配请求中携带了cookie中的username:yango 键值对 两个参数，一个是属性名称，一个是正则表达式
            - Header=X-Request-Id, \d+ #匹配请求投中要有X-Request-Id属性并且值为整数的正则表达式
```

### 过滤器

路由过滤器可用于修改进入的HTTP请求和返回的HTTP响应，路由过滤器只能指定路由进行使用。Spring Cloud Gateway 内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生。其分为执行前“pre”和执行后“post”，以及全局的和单一的过滤器。

自定义过滤器，在 cloud-gateway-gateway9527 模块中新建配置类

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Date;
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("##########come in MyLogGateWayFilter#############" + new Date());
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if (uname == null) {
            log.info("========用户名为null 非法用户=========");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
    /**
     *加载过滤器的顺序，一般数字越小优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```

重启网关服务后访问 http://localhost:9527/payment/lb?uname=ll 能够正常访问，去掉参数后访问报错

## Spring Cloud Config分布式配置中心

微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。由于每个服务都需要必要的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必不可少的。SpringCloud提供了ConfigServer来解决这个问题，我们每个微服务自己带着一个application.yml，上百个配置文件的管理......

SpringCloud Config为微服务架构中的微服务提供了集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。SpringCloud Config分为服务端和客户端两部分，服务端也称为分布式配置中心，他是一个独立的微服务应用，用来连接配置服务器并未客户端提供获取配置信息，加密/解密信息等访问接口。客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息，配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容。

作用：1.集中管理配置文件。2.不同环境不同配置文件，动态化的配置更新，分环境部署，比如dev/test/prod/release。3.运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉取，配置自己的信息。4.当配置放生变化时，服务不需要重启即可感知到配置的变化并应用新的配置。5.将配置信息以REST接口形式暴露。

### Config服务端

在github上新建仓库 springcloud-config，并新建三个配置文件 config-dev.yml、config-test.yml、config-prod.yml

新建模块 cloud-config-center3344 并修改pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-config-center3344</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 3344
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          uri: git@github.com:luguoqi/springcloud-config.git #github上面的git仓库名
          search-paths: #搜索目录
            - springcloud-config
      label: master #读取分支
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
@SpringBootApplication
@EnableConfigServer
public class ConfigCentMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCentMain3344.class, args);
    }
}
```

启动后访问 http://config-3344.com:3344/master/config-dev.yml 能够正常看到github仓库中对应文件的内容

http://config-3344.com:3344/master/config-prod.yml读取master的配置文件，而http://config-3344.com:3344/dev/config-prod.yml读取的则是dev分支上的配置文件。http://config-3344.com:3344/config-prod.yml 则默认读取master上的配置文件。如果访问不存在的配置文件则会返回"{}"而不是报错。也可以访问 http://config-3344.com:3344/config/master/master。即

### Config客户端

application.yml是用户级的资源配置

bootstrap.yml是系统级的，优先级更高

Spring Cloud会创建一个“Bootstrap Context”，作为Spring应用的“Application Context”的父上下文。初始化的时候，“Bootstrap Context”负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的“Environment”。Bootstrap属性有高优先级，默认情况下，他们并不会被本地配置覆盖，“Bootstrap Context”和“Application Context”有着不同的约定，所以新增了一个“bootstrap.yml”文件，保证“Bootstrap Context” 和“Application Context”配置的分离。要将Client模块下的application.yml文件改为bootstrap.yml，这是很关键的，因为bootstrap.yml是比application.yml先加载的，bootstrap.yml优先级高于application.yml。

新建模块 cloud-config-client3355 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-config-client3355</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>2.2.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新增bootstrap.yml配置文件

```yml
server:
  port: 3355
spring:
  application:
    name: config-client
  cloud:
    #config客户端配置
    config:
      label: master #分支名称
      name: config #配置文件名称
      profile: dev #读取后缀名称，上述三个综合：master分支上config-dev.yml的配置文件被读取 http://config-3344.com:3344//master.config-dev.yml
      uri: http://localhost:3344 #配置中心地址
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

新增主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMain3355 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain3355.class, args);
    }
}
```

新增测试controller

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
```

接着启动 cloud-eureka-server7001、cloud-config-center3344、cloud-config-client3355并访问 http://localhost:3355/configInfo 能够正常返回，其结果与http://config-3344.com:3344/master/config-dev.yml地址的返回结果一致。成功实现了客户端3355访问SpringCloud Config3344通过GitHub获取配置信息。

接下来我们修改GitHub上master分支下的config-dev.yml文件的版本号为2，这时候访问http://config-3344.com:3344/master/config-dev.yml地址，可以看到版本号变成了2，但是我们访问 http://localhost:3355/configInfo 却发现版本号仍是1，除非重启或者重新加载。

### 客户端动态刷新

github上配置文件修改后如果不重启客户端系统，无法及时加载到修改后的内容

修改 cloud-config-client3355 模块添加监控依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

修改配置文件，对外暴露监控端口

```yml
server:
  port: 3355
spring:
  application:
    name: config-client
  cloud:
    #config客户端配置
    config:
      label: master #分支名称
      name: config #配置文件名称
      profile: dev #读取后缀名称，上述三个综合：master分支上config-dev.yml的配置文件被读取 http://config-3344.com:3344//master.config-dev.yml
      uri: http://localhost:3344 #配置中心地址
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
#暴露监控端口
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

在接口上添加注解 @RefreshScope 

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
@RefreshScope
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
```

此时启动 cloud-eureka-server7001、cloud-config-center3344、cloud-config-client3355，启动后访问 http://config-3344.com:3344/master/config-dev.yml 和 http://localhost:3355/configInfo 能够正常访问，此时调整github上对应的配置文件版本号，再次访问，可以看到3355并未及时刷新。此时需要发送post请求刷新3355，才能正常看到修改后的配置信息。刷新请求地址 http://localhost:3355/actuator/refresh

## SpringCloud Bus消息总线

接上一节内容，有一个微服务，执行手动刷新还好，要是有多个在执行手动刷新就很麻烦了。要实现自动刷新配置功能，SpringCloud Bus配合SpringCloud Config 使用可以实现配置的动态刷新。其支持两种消息代理，RabbitMQ和Kafka。SpringCloud Bus是用来将分布式系统的节点与轻量级消息系统连接起来的框架，它整合了Java的事件处理机制和消息中间件的功能。

总线：在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来，由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线，在总线上的各个实例，都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息。

原理：ConfigClient实例都监听MQ中同一个topic(默认是springcloudBus)。当一个服务刷新数据的时候，他会把这个消息放入Topic中，这样其他监听统一Topic的服务就能得到通知，然后去更新自身配置。

### 案例

首先安装RabbitMQ。然后新建模块 cloud-config-client3366 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-config-client3366</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>2.2.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 3366
spring:
  application:
    name: config-client
  cloud:
    config:
      label: master #分支名称
      name: config # 配置文件名称
      profile: dev #读取后缀名称
      uri: http://localhost:3344 #配置中心地址
#      ribbitmq相关配置 15672是Web管理界面的端口，5672是MQ访问的端口
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
#暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMain3366 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain3366.class, args);
    }
}
```

新建测试controller

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
@RefreshScope
public class ConfigClientController {
    @Value("${server.port}")
    private String serverPort;
    @Value("${config.info}")
    private String configInfo;
    @GetMapping("/configInfo")
    public String getConfigInfo() {
        return "serverPort：" + serverPort + "\t configInfo:" + configInfo;
    }
}
```

注意需要在3344、3355、3366添加消息总线的依赖和配置

```yml
#      ribbitmq相关配置 15672是Web管理界面的端口，5672是MQ访问的端口
spring: 
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

最后启动 cloud-eureka-server7001、cloud-config-center3344、cloud-config-client3355、cloud-config-client3366。都启动后分别访问 http://config-3344.com:3344/master/config-dev.yml、http://localhost:3355/configInfo、http://localhost:3366/configInfo。返回了github上的同一份配置信息。然后我们来修改github上对应配置文件的版本号 ，此时访问3344对应的地址配置信息已经变更为最新。而3355、3366则还是老的版本。此时我们刷新3344配置服务 http://config-3344.com:3344/actuator/bus-refresh 后再次访问3355和3366可以看到配置已经变更为最新。达到了一次修改，广播通知，处处生效。

设计思想：

1.利用消息总线触发一个客户端/bus/refresh，而刷新所有客户端的配置。

​		打破了微服务的职责单一性，因为微服务本身是业务模块，他并不应该承担配置刷新的职责。破坏了微服务各节点的对等性。有一定的局限性，例如微服务在迁移时，他的网络地址常常会发生变化，此时想要做到自动刷新，就会增加更多的修改。

2.利用消息总线触发一个服务端ConfigServer的/bus/refresh端点，而刷新所有客户端的配置

如果不想全部通知，只想定点通知某个微服务。即指定具体某一个实例生效而不是全部，访问http://配置中心地址/actuator/bus-refresh/{destination} 。/bus/refresh请求不再发送到具体的服务实例上，而是发给config server并通过destination参数类指定需要更新配置的服务或实例。

这时我们再次修改github上对应配置的版本号，并请求 http://config-3344.com:3344/actuator/bus-refresh/config-client:3355 (config-client即为注册进eureka中的服务名)之后访问3355和3366，发现只有3355服务的配置变成了github上最新的配置。

## Spring Cloud Stream消息驱动

屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型。官方定义SpringCloud Stream是一个构建消息驱动微服务的框架，应用程序通过inputs或者outputs来与SpringCloud Stream中的binder对象交互，通过我们配置来binding(绑定)，而Spring Cloud Stream的binder对象负责与消息中间件交互。所以我们只需要搞清楚如何与SpringCloud Stream交互就可以方便使用消息驱动的方式。通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动。Spring Cloud Stream为一些供应商的消息中间件产品提供了个性化的自动化配置实现，引用了发布-订阅、消费组、分区三个核心概念、目前支持RabbitMQ、Kafka。他是用于构建与共享消息传递系统连接的高度可伸缩的事件驱动微服务框架，该框架提供了一个灵活的编程模型，它建立在已经建立和熟悉的Spring熟语和最佳实践上，包括支持持久化的发布/订阅、消费组、以及消息分区这三个核心概念。

标准的MQ生产者/消费者之间靠消息媒介传递内容，消息必须走特定的通道(消息通道MessageChannel)，消息通道MessageChannel的子接口SubscribableChannel，由MessageHandler消息处理器所订阅。中间件的差异导致我们实际项目开发给我们造成了一定的困扰，我们如果用了两个消息队列中的其中一种，后面的业务需求，我想往另一种消息队列进行迁移，这时候无疑就是一个灾难，一大堆东西都要重新推到重新做，因为它跟我们的系统耦合了，这时候springcloud Stream给我们提供了一种解耦合的方式。

在没有绑定器这个概念的情况下，我们的SpringBoot应用要直接与消息中间件进行信息交互的时候，由于各消息中间件构建的初衷不同，他们的实现细节上会有较大的差异性，通过定义绑定器作为中间层，完美地实现了应用程序与消息中间件细节之间的隔离。通过像应用程序暴露统一的Channel通道，使得应用程序不需要再考虑各种不同的消息中间件实现。通过定义Binder作为中间层，实现了应用程序与消息中间件细节之间的隔离。其中Imput对应于消费者，Output对应于生产者。Stream对消息中间件的进一步封装，可以做到代码层面对中间件的无感知，甚至于动态的切换中间件(rabbitmq和kafka)，使得微服务开发的高度解耦，服务可以关注更多自己的业务流程。

Binder：很方便的连接中间件，屏蔽差异

Channel：通道，是队列Queue的一种抽象，在消息通讯系统中就是实现存储和转发的媒介，通过Channel对队列进行配置

Source和Sink：简单的可理解为参照对象时Spring Cloud Stream自身，从Stream发布消息就是输出，接受消息就是输入。  

首先确保自己的RabbitMQ环境已经正常。

### 生产者

新建模块 cloud-stream-rabbitmq-provider8801 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-stream-rabbitmq-provider8801</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 8801
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 #设置心跳的时间间隔(默认是30秒)
    lease-expiration-duration-in-seconds: 5 #如果现在超过了5秒的间隔
    instance-id: send-8801.com #在信息；；列表显示的主机名称
    prefer-ip-address: true #访问的路径变为ip地址
spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: # 在此处配置要绑定的rabbitMQ的服务信息
        defaultRabbit: # 表示定义的名称，用于binding的整合
          type: rabbit # 消息中间件类型
          environment: # 设置rabbitMQ的相关环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: # 服务的整合处理
        output: # 这个名字是一个通道的名称
          destination: studyExchange # 表示要使用的exchange名称定义
          content-type: application/json # 设置消息类型，本次为json，文本则设为text/plain
          binder: defaultRabbit # 设置要绑定的消息服务的具体设置
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class StreamMQMain8801 {
    public static void main(String[] args) {
        SpringApplication.run(StreamMQMain8801.class, args);
    }
}
```

新建业务接口及service

```java
import com.yango.springcloud.service.IMssageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
@RestController
@Slf4j
public class SendMessageController {
    @Resource
    private IMssageProvider mssageProvider;
    @GetMapping("/sendMessage")
    public String sendMessage() {
        return mssageProvider.send();
    }
}
```

```java
public interface IMssageProvider {
    public String send();
}
```

```java
import com.yango.springcloud.service.IMssageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import javax.annotation.Resource;
import java.util.UUID;
@EnableBinding(Source.class) //指通道channel和exchange绑定在一起,定义消息的推送管道
@Slf4j
public class MessageProviderImpl implements IMssageProvider {
    @Resource
    private MessageChannel output; //消息发送通道
    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        log.info("+++++++++++++++serial:" +serial);
        return serial;
    }
}
```

启动 cloud-eureka-server7001、RabbitMQ、cloud-stream-rabbitmq-provider8801三个服务，查看MQ控制台可以看到已经创建了名为 studyExchange 的交换器。然后请求 http://localhost:8801/sendMessage 后查看MQ控制台已经产生相关消息。

### 消费者

新建模块 cloud-stream-rabbitmq-consumer8802 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-stream-rabbitmq-consumer8802</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 8802
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 #设置心跳的时间间隔(默认是30秒)
    lease-expiration-duration-in-seconds: 5 #如果现在超过了5秒的间隔
    instance-id: receive-8802.com #在信息；；列表显示的主机名称
    prefer-ip-address: true #访问的路径变为ip地址
spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息
        defaultRabbit: #表示定义的名称，用于binding整合
          type: rabbit #消息组件的类型
          enviroment: #设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置text/json
          binder: defaultRabbit #设置要绑定的消息服务的具体设置
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class StreamMQMain8802 {
    public static void main(String[] args) {
        SpringApplication.run(StreamMQMain8802.class, args);
    }
}
```

新建消费者接口

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {
    @Value("${server.port}")
    private String serverPort;
    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        log.info("消费者1号，------------------------>接收到的消息：" + message.getPayload() + "\t serverPort:" + serverPort);
    }
}
```

启动cloud-eureka-server7001、cloud-stream-rabbitmq-provider8801、cloud-stream-rabbitmq-consumer8802后多次访问 http://localhost:8801/sendMessage 查看8802的控制台，可以看到打印出8801产生的消息信息。

### 分组消费与持久化

新建模块 cloud-stream-rabbitmq-consumer8803 并修改pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud</artifactId>
        <groupId>com.yango.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-stream-rabbitmq-consumer8803</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

新建配置文件

```yml
server:
  port: 8803
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 #设置心跳的时间间隔(默认是30秒)
    lease-expiration-duration-in-seconds: 5 #如果现在超过了5秒的间隔
    instance-id: receive-8803.com #在信息；；列表显示的主机名称
    prefer-ip-address: true #访问的路径变为ip地址
spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息
        defaultRabbit: #表示定义的名称，用于binding整合
          type: rabbit #消息组件的类型
          enviroment: #设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置text/json
          binder: defaultRabbit #设置要绑定的消息服务的具体设置
```

新建主启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
public class StreamMQMain8803 {
    public static void main(String[] args) {
        SpringApplication.run(StreamMQMain8803.class, args);
    }
}
```



```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {
    @Value("${server.port}")
    private String serverPort;
    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        log.info("消费者2号，------------------------>接收到的消息：" + message.getPayload() + "\t serverPort:" + serverPort);
    }
}
```

启动cloud-eureka-server7001、cloud-stream-rabbitmq-provider8801、cloud-stream-rabbitmq-consumer8802、cloud-stream-rabbitmq-consumer8803后请求 http://localhost:8801/sendMessage 发现8802和8803两个微服务都消费了同一条消息。

比如在如下场景中，订单系统我们做集群部署，都会从RabbitMQ中获取订单信息，那如果一个订单同时被两个微服务获取到，那么就会造成数据错误，我们得避免这种情况，这时我们可适用Stream中的消息分组来解决，注意在Stream中处于同一个group中的多个消费者是竞争关系，就能够保证消息只会被其中一个应用消费一次，不同组是可以全面消费的(重复消费)。默认情况下，分组group是不同的，组流水号不一样，被认为是不同组，可以消费。微服务应用放置于同一个group中，就能保证消息只会被其中一个应用消费一次，不通过组是可以消费的，同一个组内会发生竞争关系，只有其中一个可以消费。

自定义分组，分别修改cloud-stream-rabbitmq-consumer8802、cloud-stream-rabbitmq-consumer8803的配置文件

```yml
server:
  port: 8802
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 #设置心跳的时间间隔(默认是30秒)
    lease-expiration-duration-in-seconds: 5 #如果现在超过了5秒的间隔
    instance-id: receive-8802.com #在信息；；列表显示的主机名称
    prefer-ip-address: true #访问的路径变为ip地址
spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息
        defaultRabbit: #表示定义的名称，用于binding整合
          type: rabbit #消息组件的类型
          enviroment: #设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置text/json
          binder: defaultRabbit #设置要绑定的消息服务的具体设置
          group: yangoA #自定义组名
```

```yml
server:
  port: 8803
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    lease-renewal-interval-in-seconds: 2 #设置心跳的时间间隔(默认是30秒)
    lease-expiration-duration-in-seconds: 5 #如果现在超过了5秒的间隔
    instance-id: receive-8803.com #在信息；；列表显示的主机名称
    prefer-ip-address: true #访问的路径变为ip地址
spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: #在此处配置要绑定的rabbitmq的服务信息
        defaultRabbit: #表示定义的名称，用于binding整合
          type: rabbit #消息组件的类型
          enviroment: #设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      bindings: #服务的整合处理
        input: #这个名字是一个通道的名称
          destination: studyExchange #表示要使用的Exchange名称定义
          content-type: application/json #设置消息类型，本次为json，文本则设置text/json
          binder: defaultRabbit #设置要绑定的消息服务的具体设置
          group: yangoB #自定义组名
```

重启后访问 http://localhost:8801/sendMessage 登录MQ控制端，发现其组名已经变为我们自定义的名称，但一条消息仍会被两个服务消费，此时将8803的group改为yangoA，再次重启后访问8801，发现每发一个消息只有一个服务消费到。分布式微服务应用为了实现高可用和负载均衡，实际上都会部署多个实例，本例我们启动两个消费微服务，8802/8803多数情况下，生产者发送消息给某个具体微服务时只希望别消费一次，按照上面我们启动两个应用的例子，虽然它们鼠疫同一个应用，但是这个消息出现了重复消费两次的情况，为了解决这个问题，在SpringCloud Stream中提供了消费组的概念

此时我们停掉8802和8803，并去掉8803下的group: YangoA，然后我们访问 http://localhost:8801/sendMessage 重新发送消息。然后再重启8802和8803，可以看到8803由于去掉了group的配置并没有去消费停机时发送的消息，而8802启动后则能正常消费。所以group属性在消息重复消费和持久化消费方面是一个比较重要的属性。

## Spring Cloud Sleuth分布式请求链路追踪

在微服务框架中，一个由客户端发起的请求在后端系统中会经过多个不同的服务节点调用，来协同产生最后的请求结果，每一个前段请求都会形成一个复杂的分布式服务调用链路，链路中的任何一环出现高延迟或错误都会引起整个请求最后的失败。Spring Cloud Sleuth提供了一套完整的服务跟踪的解决方案，在分布式系统中提供追踪解决方案并且兼容支持了zipkin。

SpringCloud从F版起已经不需要自己构建Zipkin Server了，只需要调用jar包即可。

下载zipkin server http://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/

下载后运行java -jar zipkin-server-2.12.9-exec.jar 看到相关图案。访问 http://localhost:9411/zipkin/ 控制台。

Trace：类似于树结构的Span集合，表示一条调用链路，存在唯一标识。

span：表示调用链路来源，通俗的理解span就是一次请求信息。

修改 cloud-provider-payment8001和cloud-customer-order80工程，添加依赖并修改配置文件

```xml
<!--包含了sleuth+zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

```yml
spring:
  zipkin:
    base-url: http://localhost:9411
  sleuth:
    sampler: #采样值介于0-1之间，1则表示全部采集
      probability: 1
```

在cloud-provider-payment8001模块中新增接口

```java
/**
 * 测试链路追踪
 */
@GetMapping("/zipkin")
public String paymentZipkin() {
    return "zipkin 请求成功";
}
```

在cloud-customer-order80模块中新增接口

```java
@GetMapping("/payment/zipkin")
public String paymentZipkin() {
    return template.getForObject("http://localhost:8001" + "/payment/zipkin", String.class);
}
```

重启cloud-eureka-server7001、cloud-provider-payment8001、cloud-customer-order80后访问 http://localhost/consumer/payment/zipkin 然后查看 zipkin控制台 http://localhost:9411/zipkin/dependency?endTs=1613573234642&startTs=1613486834643 可以看到整个请求的流程。

## Spring Cloud Alibaba

SpringCloud Netflix项目进入维护模式















































































































































