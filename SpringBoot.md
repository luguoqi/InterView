# SpringBoot

## 一、Spring Boot入门

简介：Spring Boot是用来简化Spring应用开发，是Spring技术栈的一个整合，J2EE开发的一站式解决方案，其提倡约定大于配置。解决了J2EE笨重的开发、繁多的配置、低下的开发效率、复杂的部署流程、第三方集成难度大等等问题。

其主要优点如下

​	1.创建一个独立的Spring应用。

​	2.直接内置Tomcat, Jetty或Undertow不需要部署war包

​	3.提供一些固定的'starter'依赖，来简化你的构建和配置(依赖管理和版本管理)。

​	4.大量的自动配置Spring和第三方依赖，简化开发。

​	5.提供生产的运行时的监控功能，例如指标监控、健康检查、外部化配置

​	6.绝对不需要代码自动生成和xml配置，开箱即用

**spring-boot-start：**场景启动器，相当于是某一功能场景的所有依赖，是一组依赖的组合

**1@SpringBootApplication**：springboot应用，标注在某个类上说明这个类是springboot的主配置类，springboot就应该运行这个类的main方法来启动springboot应用。

```java
// SpringBootApplication 源码
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication
```

**1.1@SpringBootConfiguration**：springboot的配置类，标注在某个类上，说明这个类是springboot的配置类，其底层是使用spring的@Configuration。

```java
//SpringBootConfiguration 源码
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration
```

**1.2@Configuration**：标注配置类的注解，配置类就相当于xml配置文件的功能,其底层也是容器中的一个组件@Component

```java
//Configuration 源码
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration
```

**2@EnableAutoConfiguration**：开启自动配置功能，spring时我们需要在xml中配置的内容，通过这个注解可以帮助我们自动配置，相当于我们跑springboot的helloworld的时候什么都不用配置，就可以直接运行一个接口，其就是依赖于这个注解

```java
//EnableAutoConfiguration 源码
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage//
@Import({AutoConfigurationImportSelector.class})//
public @interface EnableAutoConfiguration   
```

**2.1@AutoConfigurationPackage**：自动配置包，其是使用spring底层注解@Import，实现给容器中导入一个组件(即{Registrar.class}组件类)

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({Registrar.class})
public @interface AutoConfigurationPackage
```

**2.2@Import**:帮助我们将多个配置类导入到主配置中，即给容器中导入组件相当于配置文件中的 <import resource="cms-validator-service.xml"/>  

**2.3Registrar.class**：组件，将主配置类(@SpringBootApplication标注类)的所在包及其子包中的所有组件扫描到Spring容器中

```java
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {
        Registrar() {
        }
    //注册一些bean定义信息
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            AutoConfigurationPackages.register(registry, (String[])(new AutoConfigurationPackages.PackageImports(metadata))
                                               .getPackageNames().toArray(new String[0]));
        }
        public Set<Object> determineImports(AnnotationMetadata metadata) {
            return Collections.singleton(new AutoConfigurationPackages.PackageImports(metadata));
        }
    }
```

**2.4AutoConfigurationImportSelector.class**(对应@EnableAutoConfiguration源码中)：导入哪些组件的选择器，其会将所有导入的组件以全类名的方式返回，这些组件就会被添加到容器中。其最终会给容器中导入非常多的自动配置类(xxxAutoConfiguration)，其作用就是给容器中导入这个场景需要的所有组件并配置好，其免去了我们手动编写配置注入功能组件等的工作。

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223127939.png" alt="image-20201217223127939" style="zoom: 50%;" />

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223231272.png" alt="image-20201217223231272" style="zoom:50%;" />

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223327491.png" alt="image-20201217223327491" style="zoom:50%;" />

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223418175.png" alt="image-20201217223418175" style="zoom:50%;" />

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223517513.png" alt="image-20201217223517513" style="zoom:50%;" />

<img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20201217223704917.png" alt="image-20201217223704917" style="zoom:50%;" />

总结：springboot在启动的时候从类路径下META-INF/spring.factories中获取EnableAutoConfiguration指定的值，并将这些值作为自动配置类导入到容器中，这样就能帮我们进行自动配置工作。

其所有自动配置功能的实现入口都在spring-boot-autoconfigure-2.3.5.RELEASE.jar中。

## 二、Springboot应用介绍

我们通过Spring Initializer快速创建的Springboot项目默认是如下结构

- 主程序已经生成
- resource文件夹中目录结构
  - static：保存所有静态资源，如js、css、images...
  - templates：保存所有的模板页面，(Springboot默认jar包使用嵌入式Tomcat，默认不支持JSP)，但是可以使用模板引擎，如freeMark、thymeleaf...
  - application.properties：Springboot的默认配置文件，可以修改一些默认设置项

SpringBoot应用默认继承spring-boot-starter-parent，父项目统一管理依赖及依赖版本

## 三、Springboot配置

### 1.配置文件

Springboot默认支持两种配置文件，application.properties和application.yaml，并且其名字是固定的，默认就交applicationXXX.yaml，其作用是为了修改应用自动配置的默认值(即自动配置好的功能)。

YAML(YAML Ain't Markup Language)：以数据为中心，比json、xml等更适合做配置文件，如下对比

```yaml
#YAML
server:
  port: 7002
#xml
<server>
	<port>7002</port>
</server>
#properties
server.port=7002
```

### 2.基本语法

即使用k: v表示一对键值对(冒号后面的空格必须有)

- 使用缩进表示层级关系
- 缩进时不能用Tab键，只能使用空格
- 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
- 大小写敏感

### 3.数据结构

- 对象：键值对的集合 (键值对属性)。

  - 在下一行来写对象的属性和值的关系，注意缩进，例如objk对象中有属性和值k1,v1；k2,v2则书写如下

    objk: 

    ​     k1: v1

    ​	 k2:v2

    或者 objk: {k1: v1, k2: v2}  注意空格	

- 数组：一组按次序排列的值(List、Set等)。

  - 用-值表示数组中的一个元素，例如

    pets:

    ​    - cat

    ​	- dog

    ​	- pig

    注意空格

    或者pets: [cat,dog,pig]

- 字面量：单个的，不可再分的值(数字、字符串、布尔值等)。

  - k: v直接表示，数字、字符串、布尔值等不用添加引号
  - 双引号不会转义字符串里面的特殊字符，特殊字符会作为本身想表示的意思，如name: "aa \n bb"；输出aa 换行 bb
  - 单引号会转义特殊字符，特殊字符只是作为一个普通字符串，如name: "aa \nbb"；输出aa \n bb

使用注解@ConfigurationProperties(prefix = "person")，并且必须结合@Component将次组件放入容器中才能使用。另外还可以使用@Value("${person.name}")注入属性值，但是@ConfigurationProperties支持JSR303数据校验，而@Value注解只能获取基本类型的值，获取不到map、list等等的值,两者默认都是从全局配置文件中获取值

@PropertySource：加载指定配置文件。比如说我要注入person.yaml配置文件中的值到person对象中
@ImportResource：导入Spring的配置文件，让配置文件中的内容生效。相当于我们自己编写了beans.xml配置文件，如果想让其生效可以使用此注解标注在一个配置类上。

Springboot推荐使用配置类(@Configuration+@Bean，注意Bean的ia就是方法名)的形式往容器中添加组件

另外在Springboot配置文件中也可以使用${}的形式获取配置的值或者一些随机值，例如${random.uuid}如果获取的值没有则会将${k1}默认为字符串作为值，这时就可以使用默认值来解决${k1:defaultVal}

Profile:是spring对不同环境提供不同配置功能的支持，可以通过激活、指定参数等方式快速切换环境。



加载顺序

配置原理











































































































