# Docker

## 简介





## 安装

### 1.删除之前安装过的版本(第一次安装忽略)

```markdown
yum remove docker docker-client docker-client-latest docker-common docker-latest \
                  docker-latest-logrotate docker-logrotate docker-engine
```

### 2.添加阿里云的yum仓库(yum安装docker更快)

```markdown
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

### 3.安装docker

```markdown
yum install docker-ce docker-ce-cli containerd.io
```

### 4.启动docker

```markdown
systemctl start docker
```

### 6.查看docker状态或查看docker版本来验证安装成功

```markdown
systemctl status docker
或 docker version
查看docker配置文件是否存在
即 /etc/docker/key.json 文件
```

### 7.配置阿里云镜像仓库

首先登陆阿里云按一下步骤复制自己的镜像仓库地址

![image-20201025223316453](https://gitee.com/img/20210110232107.png)

接着新建在/etc/docker 下新建 daemon.json，并复制自己的镜像仓库地址json进去

```markdown
vim /etc/docker/daemon.json
#将一下json copy至daemon.json文件中
{
  "registry-mirrors": ["https://xxxxxxx.mirror.aliyuncs.com"]
}
#保存并退出
```

之后执行如下命令即可

```markdown
systemctl daemon-reload
systemctl restart docker

#重启之后查看是否配置成功,通过如下命令查看 Registry Mirrors 的值是否是刚才配置的镜像仓库地址
docker info
```

## Hello Wold

执行命令 docker run hello-world 页面如下

![image-20201026213211250](https://gitee.com/img/20210110232111.png)

该命令会先从本机查找是否有该镜像，如果没有则去Docker Hub上去拉取该镜像，如果也未找到则报错，否则下载该镜像到本地，然后以该镜像为模板生产容器实例运行

## 原理



## 常用命令

docker version 查看docker版本信息

docker info 查看docker系统信息，包括容器数量、镜像数量、总内存等等信息

docker --help  查看命令介绍

docker images 列出本机上的镜像。参数：-a 列出本机所有镜像；-q只显示镜像ID；--digests：显示镜像的摘要信息；--no-truc：显示完整的镜像信息(不截取镜像ID)。其每列含义 REPOSITORY：镜像仓库源；TAG：镜像标签；IMAGE ID：镜像ID；CREATED：镜像创建时间；SIZE：镜像大小。同一仓库源可以有多个TAG，代表这个仓库源的不同版本，我们使用REPOSITORY:TAG来定义不同的镜像。如果不指定一个镜像的版本标签，则docker将默认使用 [REPOSITORY:latest]镜像。

docker search 镜像名称  查询https://hub.docker.com上的镜像

docker search -s 30 tomcat 查询收藏数超过30的tomcat镜像

docker search --no-trunc tomcat 查询tomcat镜像并显示完整的摘要信息

docker --automated tomcat 显示自动构建类型的镜像

docker pull tomcat:7.5 从镜像仓库中拉取指定版本镜像

docker rmi -f hello-world ... 删除单个或多个镜像

docker rmi -f $(docker images -qa) 删除所有镜像

docker run [OPTIONS] IMAGE [COMMAND] [ARG...] 新建并运行容器

​	OPTIONS可选值：--name="容器新名字"：为容器指定一个名称;-d：后台运行容器，并返回容器ID，即启动守护式容器；-i以交互模式运行容器；-t：为容器重新分配一个伪输入终端；-P：随机端口映射；-p：指定端口个映射，有以下四种格式，ip:hostPort:containerPort。ip::containerPort。hostPort:containerPort。containerPort.

docker ps[OPTIONS] 列出所有正在运行的容器

​	OPTIONS可选值：-a：列出当前所有正在运行的容器+历史上运行过的容器；-l：显示最近创建的容器；-n：显示最近n个创建的容器；-q：静默模式，只显示容器编号；--no-trunc：不截断输出

exit 容器停止退出

ctrl+P+Q 容器不停止退出

docker start 容器ID或者容器名：启动容器

docker restart 容器ID或者容器名：重启容器

docker stop 容器ID或者容器名：停止容器

docker kill 容器ID或者容器名：强制停止容器

docker rm 容器ID ：删除已关闭的容器，加-f可以强制删除正在运行的容器

docker rm -f $(docker ps -a -q): 一次性删除所有容器

docker ps -a -q | xargs docker rm： 一次性删除所有容器。  | xargs:表示上一个命令的结果集传递给下一个命令

docker ps 列出所有正在运行的容器，-q 列出正在运行的容器；-l 列出上一运行的容器；-a 列出所有运行过的容器

docker run -d 镜像名称 以后台进程的形式运行容器，但通过docker ps命令却查看不到正在运行的容器，说明，docker容器后台运行，就必须有一个前台进程，如果docker运行的命令不是那些一直挂起的命令，就是会自动推出的。解决方案是将你要运行的程序以前台进程的形式运行即可

docker update [参数] 修改容器启动参数

docker logs -f -t --tail 容器id 查看容器日志，-t加入时间戳；-f跟随最新的日志打印；--tail显示最后多少条

docker run -d centos /bin/sh -c "while true;do echo hello lugq;sleep 2;donedocker "

docker inspect 容器id 查看容器内部细节

docker exec -it 容器ID bashShell 在容器中执行bashShell命令并返回结果(并不进入容器)

docker attach 容器ID 直接进入容器相当于 docker exec -t 容器id /bin/bash 命令

docker cp 容器id:容器内路径 目的主机路径   从容器中拷贝文件到主机上 docker cp 55b51:/tmp/a.txt /root

docker镜像：轻量级、可执行的独立软件包，用来打包软件运行环境和基于运行环境开发的软件，它包含运行某个软件所需的所有内容，包括代码、库、环境变量和配置文件。底层原理就是联合文件系统

docker commit 提交容器副本使之成为一个新的镜像 命令：docker commit -m="提交的描述信息" -a="作者" 容器id 要创建的目标镜像名:[标签名]  例：docker commit -a="lugq" -m="制作镜像" 625b734f984e test/mytomcat

docker run -it -v /宿主机绝对目录:/容器内目录 镜像名  创建数据卷，用于容器间共享数据或者持久化数据，如果报错cannot open directory.:Permission denied 则在后面添加参数--privileged=true即可

docker run -it -v /宿主机绝对目录:/容器内目录:ro 镜像名  创建只读镜像(容器内只读)

​		出于可移植和分享的考虑，用-v主机目录:容器目录这种方法不能直接在Dockerfile中实现，由于宿主机目录是依赖于特定宿主机的，并不能够保证在所有宿主机上都存在这样的特定目录

docker build -f dockerfile路径 -t 命名空间/镜像名称 .    根据dockerfile构建镜像，只支持在容器内添加。

数据卷容器：命名的容器挂载数据卷，其他容器通过挂载这个(父容器)实现数据共享，挂载数据卷的容器，称之为数据卷容器

dockerfile：是docker镜像的构建文件，构建步骤为：编写Dockerfile文件->执行docker build->执行docker run

​		其内的每条指令都会创建一个新的镜像蹭，并对镜像进行提交

FROM：基础镜像，当前新镜像是基于那个镜像的

MAINTAINER：镜像维护者的姓名和邮箱地址

RUN：容器构建时需要运行的命令(可以执行linux命令)

EXPOSE：当前容器对外暴露的出的端口

WORKDIR：指定在创建容器后，终端默认登录进来的工作目录，一个落脚点

ENV：用来在构建镜像的过程中设置环境变量

ADD：将宿主机目录下的文件拷贝进镜像且会自动处理URL和解压tar压缩包

COPY：类似ADD，拷贝文件和目录到镜像中，将从构建上下文目录中<源路径>的文件/目录复制到新的一层的镜像内的<目标路径>位置

VOLUME：容器数据卷，用于数据保存和持久化工作

CMD：指定一个容器启动时要运行的命令，Dockerfile中可以有多个CMD命令，但只有最后一个生效，CMD会被docker run之后的参数替换，例如执行docker run -it -p 7777:8080 tomcat ls -l只会 列出tomcat下的文件，并不会启动tomcat

ENTRYPOINT：指定一个容器启动时要运行的命令，ENTRYPOINT的目的和CMD一样，都是在指定容器启动程序及参数，这个在docker run 之后追加

ONBUILD：当构建一个被继承的Dockerfile时运行命令，父镜像在被子继承后父镜像的onbuild被触发，也就是说等通过Dockerfile构建子镜像时，父镜像的的ONBUILD被触发

Base镜像(scratch)：Docker Hub中99%的镜像都是通过在base镜像中安装和配置需要的软件构建出来的

案例：修改centos启动进入时的落脚点为/usr/local，并支持vim 和ifconfig命令

```shell
FROM centos
MAINTAINER xiaoyu<xiaoyu@126.com>
ENV MYPATH /usr/local
WORKDIR $MYPATH
RUN yum install -y vim
RUN yum install -y net-tools

EXPOSE 80

CMD echo $MYPATH
CMD echo "success----------------------------ok"
CMD /bin/bash
```

查看docker启动时的参数

```shell
yum install npm -y
npm i -g rekcod
docker ps -qa|rekcod
```









































