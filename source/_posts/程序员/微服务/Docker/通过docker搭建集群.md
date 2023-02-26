---
title: 通过docker搭建集群  
date: 2022-12-04 02:36:27  
tags: []  
categories:
  - 微服务
  - Docker
---
1. 首先下载最新版的centos

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326003243.png)

此时的 centos 是最简版本，很多基本命令都没有。

2. 写一个Dockerfile，通过centos，设置密码为123456，安装ssh服务并启动

```shell
FROM centos
MAINTAINER mwf

RUN yum install -y openssh-server sudo
RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config
RUN yum  install -y openssh-clients

RUN echo "root:123456" | chpasswd
RUN echo "root   ALL=(ALL)       ALL" >> /etc/sudoers
RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key

RUN mkdir /var/run/sshd
EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
```

3. 通过Dockerfile创建镜像centos7-ssh

```shell
[root@VM-11-161-centos /home/docker/centos]# docker build -t="centos7-ssh" .
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326003750.png)

4. 通过该镜像进一步生成带有我们所需要的工作环境的镜像

```shell
# base image
FROM centos7-ssh

# MainTainer
MAINTAINER CoachHe <coachhe@tencent.com>


RUN yum -y install vim
RUN yum -y install net-tools
RUN mkdir /opt/module
RUN mkdir /opt/software
RUN yum install ncurses
RUN yum install rsync


ADD jdk-8u144-linux-x64.tar.gz /opt/module
ADD hadoop-2.7.2.tar.gz /opt/module
ADD apache-hive-1.2.1-bin.tar.gz /opt/module
COPY mysql-libs.zip /opt/software


ENV JAVA_HOME /opt/module/jdk1.8.0_144
ENV HADOOP_HOME /opt/module/hadoop-2.7.2
ENV HIVE_HOME /opt/module/apache-hive-1.2.1-bin
ENV PATH $JAVA_HOME/bin:$HADOOP_HOME/bin:$HIVE_HOME/bin:$PATH

RUN yum install -y which sudo
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326010435.png)

执行结果

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326010948.png)

5. 配置网络

```shell
docker run -itd --network bridge --name chadoop1 -p 50070:50070 -p 8088:8088 chadoop
docker run -itd --network bridge --name chadoop2 chadoop
docker run -itd --network bridge --name chadoop3 chadoop
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326011132.png)

查看网络情况

```shell
docker network inspect bridge
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326011255.png)

可以看到，分别分配了三个IP地址

```shell
chadoop1 192.168.10.2
chadoop2 192.168.10.3
chadoop3 192.168.10.4
```

6. 登录docker容器

```shell
docker exec -it chadoop1 bash
docker exec -it chadoop2 bash
docker exec -it chadoop3 bash
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326011503.png)

7. 配置hosts以及ssh免密

```shell
vim /etc/hosts
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326011748.png)

ssh免密

```shell
ssh-keygen # 一路回车
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326012056.png)

```shell
ssh-copy-id -i /root/.ssh/id_rsa -p 22 root@chadoop1
ssh-copy-id -i /root/.ssh/id_rsa -p 22 root@chadoop2
ssh-copy-id -i /root/.ssh/id_rsa -p 22 root@chadoop3
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326012249.png)

8. 创建集群同步脚本xsync

```shell
mkdir /home/bin
vim /home/bin/xsync
chmod u+x /home/bin/xsync
```

脚本：

```shell
#!/bin/bash 
#1 获取输入参数个数，如果没有参数则直接退出 
pcount=$# 

if((pcount==0)); then 
    echo no args; 
    exit; 
fi 

#2 获取文件名称 
p1=$1 
fname=`basename $p1` #这里就是拿到最终文件的名称，/opt/module就是module 
echo fname=$fname 

#3 获取上级目录到绝对路径 
pdir=`cd -P $(dirname $p1); pwd` #这里是得到文件的路径 
echo pdir=$pdir 

#4 获取当前用户名称 
user=`whoami`  #也就是coachhe 

#5 循环 
for((host=1;host<4;host++)); do    
    echo -------- hadoop$host ---------
    rsync -rvl $pdir/$fname root@chadoop$host:$pdir
done
```

同步实例

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326012916.png)





启动

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210326014924.png)





# 指定容器的hostname

## chadoop01

```shell
docker run -itd --network bridge -v module01:/opt/module --name chadoop01 --hostname chadoop01 -p 50080:50080 -p 8088:8088 coachhe/chadoop:2.0
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210419094542.png)



## chadoop02

```shell
docker run -itd --network bridge -v module02:/opt/module --name chadoop02 --hostname chadoop02 -p 50081:50081 coachhe/chadoop:2.1
```



## chadoop03

```shell
docker run -itd --network bridge -v module03:/opt/module --name chadoop03 --hostname chadoop03 -p 50082:50082 coachhe/chadoop:2.1
```



```shell
docker run -itd --network bridge -v pulsar:/root --name pulsar01 --hostname pulsar01 -p 50090:50090 apachepulsar/pulsar
```






最新版：
```shell
[root@VM-11-161-centos ~]# docker run -itd --name clinux01 --hostname clinux01  -p 60122:22 -p 60123:36000 -p 60124:80 -p 60125:8080 --mount type=bind,source=/cbs/dockerVolume/clinux01,target=/root centos7-ssh
896ef5a69b1352d085cfe6e66b0803621c10e77844dc48f76c99d892892b48ba
[root@VM-11-161-centos ~]# docker run -itd --name clinux02 --hostname clinux02  -p 60222:22 -p 60223:36000 -p 60224:80 -p 60225:8080 --mount type=bind,source=/cbs/dockerVolume/clinux01,target=/root centos7-ssh
f9e7377a0612c10abdd8995a1283423927200b6bd471efcf209b2f8f6351403e
[root@VM-11-161-centos ~]# docker run -itd --name clinux03 --hostname clinux03  -p 60322:22 -p 60323:36000 -p 60324:80 -p 60325:8080 --mount type=bind,source=/cbs/dockerVolume/clinux01,target=/root centos7-ssh
1581f4a90b50fe0ac113b7e600464333f3d98e20a0b7a3d9e0dacbf7a8f6bd13
```



```shell
docker run -itd --name springcloud2022 --hostname coachhe -p 50001-60000:1-10000  --mount type=bind, source=/cbs/dockerVolume/springcloud2022, target=/root centos7-ssh
```







