---
title: Docker容器数据卷  
date: 2022-12-04 02:35:02  
tags: []  
categories:
  - 微服务
  - Docker
---
# 容器数据卷

## 什么是容器数据卷

将应用和环境打包成一个镜像！

数据？如果数据都在容器中，那么我们容器删除，数据就会丢失！需求：数据可以持久化

MySQL，容器删除了，删库跑路！需求：MySQL数据可以存储在本地！

容器之间可以有一个数据共享的技术！Docker容器中产生的数据，同步到本地！

这就是卷技术！目录的挂载，将我们容器内的目录，挂载到Linux上面！

**总结一句话：容器的持久化和同步操作！容器间也是可以数据共享的！**

## 使用数据卷

> 方法1 直接使用命令来挂载 -v

```shell
docker run -it -v 主机目录：容器内目录 镜像
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325155433.png)

之后

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325155537.png)

再来测试，关闭容器之后重新打开

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325155802.png)

依旧是同步的

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325160127.png)

**好处：我们以后修改只需要在本地修改即可，容器内会自动同步！**





### 具名和匿名挂载

```shell
# 匿名挂载
-v 容器内路径!
docker run -d -P --name nginx01 -v /etc/nginx nginx

# 查看所有的volume的情况
➜  ~ docker volume ls    
DRIVER              VOLUME NAME
local               33ae588fae6d34f511a769948f0d3d123c9d45c442ac7728cb85599c2657e50d
local            
# 这里发现，这种就是匿名挂载，我们在 -v只写了容器内的路径，没有写容器外的路劲！

# 具名挂载
➜  ~ docker run -d -P --name nginx02 -v juming-nginx:/etc/nginx nginx
➜  ~ docker volume ls                  
DRIVER              VOLUME NAME
local               juming-nginx

```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325161309.png)

卷的目录：

所有的docker容器内的卷，没有指定目录的情况下都是在`/var/lib/docker/volumes/xxxx/_data`下；

我们可以通过具名挂载方便的找到一个卷，大多情况下在使用`具名挂载`

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325162154.png)

拓展：

```shell
# 通过 -v 容器内路径： ro rw 改变读写权限
ro #readonly 只读
rw #readwrite 可读可写
docker run -d -P --name nginx05 -v juming:/etc/nginx:ro nginx
docker run -d -P --name nginx05 -v juming:/etc/nginx:rw nginx

# ro 只要看到ro就说明这个路径只能通过宿主机来操作，容器内部是无法操作！
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325163225.png)

启动自己的容器

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325164604.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210325164741.png)




