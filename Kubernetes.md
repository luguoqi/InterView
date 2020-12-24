# Kubernetes

### 简介

​		kubernetes-舵手，对应docker的鲸鱼图标，意思是指引docker的方向，可以理解成是docker的上层结构，类似于javaSE和javaEE的关系，它是以docker的标准为基础，打造一个全新的分布式架构系统，但其不一定要依赖于docker。是用于**自动部署、扩展和管理容器**化应用程序的开源系统，他将组成应用程序的容器组合成逻辑单元，以便于管理和服务发现。

1.是谷歌在2014年开源的容器化管理系统，进行容器化应用部署，有利于应用的扩展，部署容器化应用更加简洁和高效。

2.功能

1）自动装箱

​	基于容器对应用运行环境的资源配置要求自动部署应用容器

2）自我修复(自愈能力)

​	当容器失败时，会对容器进行重启，当所部署的Node节点有问题时，会对容器进行重新部署和调度，当容器未通过监控监察时，会关闭此容器直到容器正常运行时，才会对外提供服务。

3）水平扩展

  通过简单的命令，用户UI界面或基于CPU等资源使用情况，对应用容器进行规模扩大或规模裁剪

4）服务发现

​	用户不需使用额外的服务发现机制，就能够基于Kubernetes自身能力实现服务发现和负载均衡

5）滚动更新

​	可以根据应用的变化，对应用容器运行的应用，进行一次性或批量式更新

6）版本回退

​	可以根据应用部署情况，对应用容器运行的应用，进行历史版本即时回退

7）秘钥和配置管理

​	在不需要重新构建镜像的情况下，可以部署和更新秘钥和应用配置，类似热部署

8）存储编排

​	自动实现存储系统挂载及应用，特别对有状态应用实现数据持久化非常重要，存储系统可以来自于本地目录，网络存储(NFS、Gluster、Geph等)、公共云存储服务

9）批处理

​	提供一次性任务、定时任务，满足批量数据处理和分析的场景

3.组件介绍

(1)Master组件

​    apiserver，集群统一入口，以restful方式，交给etcd存储

​    scheduler，节点调度，选择node节点应用部署

​    controller-manager，处理集群中常规后台任务，一个资源对应一个控制器

​    etcd，存储系统，用于保存集群中相关的数据

(2)Node组件

​    kubelet，master派到node节点代表，管理本机容器

​    kube-proxy，提供网络代理，负载均衡等操作

核心概念

​    Pod，最小部署单元，一组容器的集合，共享网络，生命周期短暂(服务器重启或重新部署即是一个新的pod)

​    controller，确保预期的pod副本数量，无/有状态应用部署，一次性任务和定时任务

​    Service，定义一组pod的访问规则

### 集群搭建

#### kubeadm方式搭建

​	1.系统初始化

```shell
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config #永久
setenforce 0 #临时

# 关闭swap
swapoff -a #临时
sed -ri 's/.*swap.*/#&/' /etc/fstab #永久

# 设置主机名
hostnamectl set-hostname <hostname>
# 在master节点上添加hosts
cat >> /etc/hosts << EOF
192.168.136.136 master
192.168.136.135 node1
192.168.136.137 node2
EOF

# 将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

sysctl --system #生效

# 时间同步
yum install ntpdate -y
ntpdate time.windows.com
```

2.在所有节点安装Docker/kubeadm/kubelet

2.1安装Docker

```shell
# 添加阿里云的yum仓库(yum安装docker更快)
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
# 安装ce版docker
yum install docker-ce docker-ce-cli containerd.io
# 设置为开机启动
systemctl enable docker && systemctl start docker
# 查看docker版本，验证是否安装成功
systemctl status docker
或 docker version
查看docker配置文件是否存在
即 /etc/docker/key.json 文件
# 配置自己的阿里云镜像地址
cat > /etc/docker/daemon.json << EOF
{
"registry-mirrors":["https://81n87evg.mirror.aliyuncs.com"]
}
EOF
# 重新加载docker
systemctl daemon-reload
systemctl restart docker
#重启之后查看是否配置成功,通过如下命令查看 Registry Mirrors 的值是否是刚才配置的镜像仓库地址
docker info
```

2.2添加阿里云YUM软件源

```shell
cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

安装kubeadm，kubelet和kubectl

由于版本更新频繁，这里指定版本安装

```shell
yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
systemctl enable kubelet
```

部署Kubernetes Master

在Master节点上执行

```shell
kubeadm init \
--apiserver-advertise-address=192.168.136.136 \
--image-repository registry.aliyuncs.com/google_containers \
--kubernetes-version v1.18.0 \
--service-cidr=10.96.0.0/12 \
--pod-network-cidr=10.244.0.0/16
# 使用kubectl工具
mkdir -p $HOME/.kube
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config
# 查看node节点
kubectl get nodes

# 在node节点中执行
kubeadm join 192.168.136.136:6443 --token douw8l.z9ko0ttqtuqxd1r3 \
    --discovery-token-ca-cert-hash sha256:29233acbdbb05a06079b7e709859597e9a9418a277e15ab4bfa65a72836872ce
```

部署CNI网络插件

```shell
wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

# 默认镜像地址无法访问，sed命令修改为docker hub镜像仓库
kubectl apply -f \
https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
# 或者
kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml

kubectl get pods -n kube-system
# 安装nginx测试kubernetes集群
kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=NodePort
# 查看端口，并通过浏览器访问
kubectl get pod,svc
# 通过浏览器访问任意主机的ip+端口
```

#### 二进制方式搭建

系统初始化

```shell
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config #永久
setenforce 0 #临时

# 关闭swap
swapoff -a #临时
sed -ri 's/.*swap.*/#&/' /etc/fstab #永久

# 设置主机名
hostnamectl set-hostname <hostname>
# 在master节点上添加hosts
cat >> /etc/hosts << EOF
192.168.136.139 m1
192.168.136.138 n1
EOF

# 将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

sysctl --system #生效

# 时间同步
yum install ntpdate -y
ntpdate time.windows.com
```

cfssl证书生成

```shell
wget https://pkg.cfssl.org/R1.2/cfssl_linux-amd64
wget https://pkg.cfssl.org/R1.2/cfssljson_linux-amd64
wget https://pkg.cfssl.org/R1.2/cfssl-certinfo_linux-amd64

chmod +x cfssl_linux-amd64 cfssljson_linux-amd64 cfssl-certinfo_linux-amd64

mv cfssl_linux-amd64 /usr/local/bin/cfssl
mv cfssljson_linux-amd64 /usr/local/bin/cfssljson
mv cfssl-certinfo_linux-amd64 /usr/bin/cfssl-certinfo
```



















