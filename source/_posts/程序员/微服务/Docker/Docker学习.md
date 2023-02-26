---
title: Docker学习  
date: 2022-12-04 02:34:57  
tags: []  
categories:
  - 微服务
  - Docker
---
# Docker学习过程

* Docker概述
* Docker安装
* Docker命令
  * 镜像命令
  * 容器命令
  * 操作命令
  * ...
* Docker镜像！
* 容器数据卷！
* DockerFile
* Docker网络原理
* IDEA整合Docker
* Docker Compose
* Docker Swarm
* CI\CD Jenkins



# 为什么要有Docker

java -- apk -- 发布（应用商店） -- 张三使用apk -- 安装即可用！

java -- jar（环境） -- 打包项目带上环境（镜像） -- Docker仓库（商店） -- 下载我们发布的镜像-- 直接运行即可！

> 之前的虚拟机技术

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323004748.png)

可以看到，在一个内核Kernel上所有APP共用同一个Lib，导致Lib很大很繁杂，并且不同APP如果需要不同的环境则很麻烦。

此外，如果我需要部署集群，则需要完整的一套Kernel+Lib，以及非常多的APP，这会导致极大的资源浪费。

> 容器化技术

容器化技术不是模拟一个完整的操作系统

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323004650.png)

可以看到每个APP都有单独的Lib，这个Lib可以很独立很小，并且共用一个Kernel。

部署集群时可以不用重新搭建一套Kernel+Lib+App

# Docker安装

## Docker基本组成

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323005140.png)

> 镜像（image）

镜像就好比是一个模板，可以通过这个模板来创建容器服务。

通过这个镜像可以创建多个容器（最终运行或者项目运行都是在容器上）

> 容器（container）

Docker利用容器技术，独立运行一个或者一组运用，是通过镜像来创建的。

有启动，停止，删除等Linux基本指令

目前就可以把这个容器理解为一个简易的Linux系统

> 仓库

仓库就是存放镜像的地方

## 阿里云镜像加速

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323005451.png)

## 回顾HelloWorld流程

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323005607.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323005908.png)

## 底层原理

### Docker是怎么工作的？

Docker是一个Client-Server结构的系统，Docker的守护进程运行在主机上，通过Socket从客户端访问！

DockerServer接收到Docker-Client的指令，就会执行这个命令！

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323010524.png)

### Docker为什么比VM快？

1. Docker有着比虚拟机**更少的抽象层**
2. Docker利用的是宿主机的内核，VM需要的是Guest OS

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323010808.png)

所以说，新建一个容器的时候，docker不需要像虚拟机一样重新加载一个操作系统内核，避免引导。虚拟机是加载Guset OS , 分钟级别的，而docker是利用宿主机的操作系统，省略了这个复杂的过程，秒级。



# Docker的常用命令

##  帮助命令

```dockerfile
docker version 		# 显示docker的版本信息
docker info				# 显示docker的系统信息，包括镜像和容器的数量
docker 命令 --help # 万能命令
```

## 镜像命令

### **docker images** 

查看本地主机上的所有镜像

```shell
[root@VM-11-161-centos ~]# docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
hello-world         latest              bf756fb1ae65        14 months ago       13.3kB

# 解释
REPOSITORY 	镜像的仓库源
TAG 				镜像标签
IMAGE ID 		镜像的id
CREATED 		创建时间
SIZE 				镜像大小

# 可选项
-a， --all		# 列出所有镜像
-q， --quiet # 只显示镜像的id
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323235145.png)

### **docker search** 

搜索镜像

```shell
[root@VM-11-161-centos ~]# docker search mysql
NAME                              DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
mysql                             MySQL is a widely used, open-source relation…   10647               [OK]                
mariadb                           MariaDB Server is a high performing open sou…   3995                [OK]                
mysql/mysql-server                Optimized MySQL Server Docker images. Create…   779                                     [OK]
percona                           Percona Server is a fork of the MySQL relati…   528                 [OK]  


# 可选项
--filter=STARS=3000  # 搜索出来的镜像就是STARS>3000的镜像
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210323235620.png)

### **docker pull**  

拉取镜像

```shell
[root@VM-11-161-centos ~]# docker pull mysql
Using default tag: latest	## 如果不写tag，那就默认latest
latest: Pulling from library/mysql	
a076a628af6f: Pull complete 	## 分层下载，docker image的核心 联合文件系统
f6c208f3f991: Pull complete 
88a9455a9165: Pull complete 
406c9b8427c6: Pull complete 
7c88599c0b25: Pull complete 
25b5c6debdaf: Pull complete 
43a5816f1617: Pull complete 
1a8c919e89bf: Pull complete 
9f3cf4bd1a07: Pull complete 
80539cea118d: Pull complete 
201b3cad54ce: Pull complete 
944ba37e1c06: Pull complete 
Digest: sha256:feada149cb8ff54eade1336da7c1d080c4a1c7ed82b5e320efb5beebed85ae8c	## 签名信息
Status: Downloaded newer image for mysql:latest 

# 指定版本下载
[root@VM-11-161-centos ~]# docker pull mysql:5.7
5.7: Pulling from library/mysql
a076a628af6f: Already exists 	## 可以看到，和mysql共用了一个文件
f6c208f3f991: Already exists 
88a9455a9165: Already exists 
406c9b8427c6: Already exists 
7c88599c0b25: Already exists 
25b5c6debdaf: Already exists 
43a5816f1617: Already exists 
1831ac1245f4: Pull complete 
37677b8c1f79: Pull complete 
27e4ac3b0f6e: Pull complete 
7227baa8c445: Pull complete 
Digest: sha256:b3d1eff023f698cd433695c9506171f0d08a8f92a0c8063c1a4d9db9a55808df
Status: Downloaded newer image for mysql:5.7
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324000100.png)

### 删除镜像

docker rmi -f ID

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324000251.png)

## 容器命令

说明：我们有了镜像才能创建容器，linux下载一个centos来测试学习。

```shell
docker pull centos
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324000601.png)

### 新建容器并启动

```shell
docker run [可选参数] image

# 参数说明
--name="name"   容器名字 tomcat1 tomcat2用来区分容器
-d 							后台方式运行
-it							使用交互方式运行，进入容器查看内容
-p							指定容器端口 -p 8080:8080
			-p 主机端口:容器端口（常用）
			-p 容器端口
			容器短开口
-P							大写的P，随机指定端口
```

```shell
[root@VM-11-161-centos ~]# docker run -it centos /bin/bash
[root@7be417555000 /]# ls		#查看容器内的centos，基础版本，很多命令都是不完善的！
bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
[root@7be417555000 /]# exit	# 退出容器
exit
[root@VM-11-161-centos ~]# 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324002628.png)

容器内部和外部没有任何关系！

### 查看正在运行的容器

```shell
docker ps
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324002929.png)

### 列出历史运行过的容器

```shell
docker ps -a
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324003025.png)

### 只显示容器的编号

```shell
docker ps -q
```

### 退出容器

```shell
exit # 直接容器停止并退出
```

### 删除容器

```shell
docker rm ID 	# 删除镜像，不能删除正在运行的容器，如果需要强行删除需要参数-f
docker rm -f $(docker ps -aq)	# 删除全部
```

### 启动和停止容器的操作

```shell
docker start 容器id		# 启动容器
docker stop 容器id		# 停止容器
docker kill 容器id		# 杀死容器
docker restart 容器id	# 重启容器
```

## 常用其他命令

### 后台启动容器

```shell
# 命令 docker run -d 镜像名！
[root@VM-11-161-centos ~]# docker run -d centos

# 问题docker ps 发现centos停止了

# 常见的坑，docker容器使用后台运行，就必须有一个前台进程，docker发现没有应用，就会自动停止
```

### 后台启动并执行程序

```shell
docker run -d centos /bin/sh -c "while true;do echo coachhe;sleep 1; done"
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324004406.png)

### 查看日志命令

```shell
docker logs -f -t --tail 10 200e92dfefc1
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324005444.png)

### docker的top命令

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324005646.png)

### 查看镜像的元数据

```shell
docker inspect ID
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324010007.png)

### 进入当前正在运行的容器

```shell
# 我们通常容器都是使用后台方式运行的，需要进入容器，修改一些配置

# 命令
docker exec -it 容器ID bashShell
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324010414.png)

```shell
# 方式2
docker attach 容器ID
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324010821.png)



### 拷贝

#### 从容器内将文件拷贝到主机上

```shell
docker cp 容器ID:PATH 主机PATH

# 拷贝是一个手动过程，未来我们使用-v卷的技术，可以实现主机和容器的文件的自动同步
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210324011420.png)

#### 将主机上的文件拷贝进容器内

```shell
docker cp 文件目录 容器ID:PATH
```





# 作业1 安装Nginx

> Docker安装Nginx

```shell
1. 搜索镜像 search
2. 下载镜像 pull
3. 运行测试
```

​        ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325002719.png)

### 端口原理：

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325003128.png)

### 访问效果：

通过外网地址和3344端口访问到了Nginx

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325003254.png)

### 进入交互模式：

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325003431.png)

思考问题：

​	我们每次改动nginx配置文件，都需要进入容器内部？非常麻烦，要是可以在容器外部提供一个映射路径，达到在容器修改文件名，容器内部就可以直接修改？

# 作业2 使用docker装tomcat

```shell
# 官方的使用
docker run -it --rm tomcat:9.0
# 我们之前的启动都是后台，停止了容器之后，容器还是可以查到 docker run -it --rm 一般用来测试，用完即删除
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325004100.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325004118.png)

## docker下载tomcat

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325004218.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325004254.png)

### 访问效果

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325004348.png)

不能访问的原因是因为这里的tomcat是一个阉割版。阿里云镜像默认是最小的镜像，所有不必要的都剔除掉，保证最小的可运行环境。

此时我们将webapps.dist里面的文件全部拷贝进入webapps

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325005157.png)

可以看到最终的效果

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325005128.png)

# 作业3：部署es+kibana

```shell
# es 暴露的端口很多
# es 十分耗内存
# es 的数据一般需要放置到安全目录
# --net somenetwork ？ 网络配置
docker run -d --name elasticsearch --net somenetwork -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:tag
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325010624.png)

### 内存占用情况

命令 docker stats

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325010842.png)

# Docker镜像讲解

## 镜像是什么

## Docker镜像加载原理

> UnionFs（联合文件系统）

Union文件系统（UnionFs）是一种分层、轻量级并且高性能的文件系统，他支持对文件系统的修改作为一次提交来一层层的叠加，同时可以将不同目录挂载到同一个虚拟文件系统下（ unite several directories into a single virtual filesystem)。Union文件系统是 Docker镜像的基础。镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像
特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录

![](http://km.oa.com/files/photos/pictures/202010/1602687339_62_w754_h193.png)

如上图，从右侧以UFS的视角来看，lowerdir和upperdir是两个不同的目录，UFS将二者合并起来，得到merged层展示给调用方。从左侧的docker角度来理解，lowerdir就是镜像，upperdir就相当于是容器默认的可写层。在运行的容器中修改了文件，可以使用docker commit指令保存成为一个新镜像。

> Docker镜像加载原理 

docker的镜像实际上由一层一层的文件系统组成，这种层级的文件系统UnionFS。
boots(boot file system）主要包含 bootloader和 Kernel, bootloader主要是引导加 kernel, Linux刚启动时会加载bootfs文件系统，在 Docker镜像的最底层是 bootfs。这一层与我们典型的Linux/Unix系统是一样的，包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由 bootfs转交给内核，此时系统也会卸载bootfs。
rootfs（root file system),在 bootfs之上。包含的就是典型 Linux系统中的/dev,/proc,/bin,/etc等标准目录和文件。 rootfs就是各种不同的操作系统发行版，比如 Ubuntu, Centos等等。

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210325012052.png)



对于一个精简的OS,rootfs可以很小，只需要包合最基本的命令，工具和程序库就可以了，因为底层直接用Host的kernel，自己只需要提供rootfs就可以了。

由此可见对于不同的Linux发行版， boots基本是一致的， rootfs会有差別，因此不同的发行版可以公用bootfs.

**虚拟机是分钟级别，容器是秒级！**

### 封层理解

> 分层的镜像

我们可以去下载一个镜像，注意观察下载的日志输出，可以看到是一层层的在下载

![image-20210325145200076](/Users/coachhe/Library/Application Support/typora-user-images/image-20210325145200076.png)

**思考：**

为什么Docker镜像要采用这种分层的结构呢？

最大的好处，我觉得莫过于**资源共享**了！比如有多个镜像都从相同的Base镜像构建而来，那么宿主机只需在磁盘上保留一份base镜像，同时内存中也只需要加载一份base镜像，这样就可以为所有的容器服务了，而且镜像的每一层都可以被共享。

查看镜像分层的方式可以通过

```shell
docker image inspect name
```

命令

**理解：**

 所有的 Docker镜像都起始于一个基础镜像层，当进行修改或培加新的内容时，就会在当前镜像层之上，创建新的镜像层。

举一个简单的例子，假如基于 Ubuntu Linux16.04创建一个新的镜像，这就是新镜像的第一层；如果在该镜像中添加 Python包，
就会在基础镜像层之上创建第二个镜像层；如果继续添加一个安全补丁，就会创健第三个镜像层该像当前已经包含3个镜像层，如下图所示（这只是一个用于演示的很简单的例子）。
![](https://img-blog.csdnimg.cn/2020090520471054.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RERERlbmdf,size_16,color_FFFFFF,t_70#pic_center)

 在添加额外的镜像层的同时，镜像始终保持是当前所有镜像的组合，理解这一点非常重要。下图中举了一个简单的例子，每个镜像层包含3个文件，而镜像包含了来自两个镜像层的6个文件。

![](https://img-blog.csdnimg.cn/20200905204721901.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RERERlbmdf,size_16,color_FFFFFF,t_70#pic_center)

上图中的镜像层跟之前图中的略有区別，主要目的是便于展示文件
下图中展示了一个稍微复杂的三层镜像，在外部看来整个镜像只有6个文件，这是因为最上层中的文件7是文件5的一个更新版

![](https://img-blog.csdnimg.cn/20200905204732895.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RERERlbmdf,size_16,color_FFFFFF,t_70#pic_center)

 这种情況下，上层镜像层中的文件覆盖了底层镜像层中的文件。这样就使得文件的更新版本作为一个新镜像层添加到镜像当中

Docker通过存储引擎（新版本采用快照机制）的方式来实现镜像层堆栈，并保证多镜像层对外展示为统一的文件系统

Linux上可用的存储引撃有AUFS、 Overlay2、 Device Mapper、Btrfs以及ZFS。顾名思义，每种存储引擎都基于 Linux中对应的件系统或者块设备技术，井且每种存储引擎都有其独有的性能特点。

Docker在 Windows上仅支持 windowsfilter 一种存储引擎，该引擎基于NTFS文件系统之上实现了分层和CoW [1]。

下图展示了与系统显示相同的三层镜像。所有镜像层堆并合井，对外提供统一的视图

![](https://img-blog.csdnimg.cn/20200905204745980.png#pic_center)

> 特点

Docker镜像都是只读的，当容器启动时，一个新的可写层加载到镜像的顶部！

这一层就是我们通常说的**容器层**，容器层之下都叫镜像层！



# commit 镜像

```shell
docker commit 提交容器成为一个新的副本

# 命令和git原理类似
docker commit -m="描述信息" -a="作者" 容器id 目标镜像名:[TAG]
```

### 实战

```shell
# 1、启动一个默认的tomcat
docker run -d -p 8080:8080 tomcat
# 2、发现这个默认的tomcat 是没有webapps应用，官方的镜像默认webapps下面是没有文件的！
docker exec -it 容器id
# 3、拷贝文件进去

# 4、将操作过的容器通过commit调教为一个镜像！我们以后就使用我们修改过的镜像即可，这就是我们自己的一个修改的镜像。
docker commit -m="描述信息" -a="作者" 容器id 目标镜像名:[TAG]
docker commit -a="kuangshen" -m="add webapps app" 容器id tomcat02:1.0
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210419092617.png)


# docker 启动命令
```shell
docker run -itd --name tlinux --hostname tlinux -p 60001-60500:1-500 -p 60501:36000 -p 60502:8080 -p 60503-61000:10503-11000 --mount type=bind,source=/dockerVolume/,target=/root csighub.tencentyun.com/official/tlinux-2.2-base /bin/bash
```

















