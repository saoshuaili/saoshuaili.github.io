---
title: jdk编译过程（Centos）  
date: 2022-12-04 02:22:33  
tags: []  
categories:
  - 编程语言
  - Java
  - jdk
---
# 版本

## 系统版本
```xml
[root@coachhe-code ~]# uname -srm
Linux 3.10.107-1-tlinux2_kvm_guest-0049 x86_64
```

## Gcc 版本

```xml
[root@coachhe-code TencentKona-11-master]# gcc --version
gcc (GCC) 8.5.0 20210514 (Red Hat 8.5.0-4)
Copyright (C) 2018 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
```


## G++ 版本
```
[root@coachhe-code TencentKona-11-master]# g++ --version
g++ (GCC) 8.5.0 20210514 (Red Hat 8.5.0-4)
Copyright (C) 2018 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
```


## 最终 jdk 版本
```xml
[root@coachhe-code bin]# ./java -version
openjdk version "11.0.13-internal" 2021-10-19
OpenJDK Runtime Environment (build 11.0.13-internal+0-adhoc..TencentKona-11-master)
OpenJDK 64-Bit Server VM (build 11.0.13-internal+0-adhoc..TencentKona-11-master, mixed mode)
```



# 安装过程
1. 下载 jdk11
注意我这里是腾讯的 jdk11，不是市面上的 openjdk11，源码下载地址是：[TencentKona-11](https://github.com/Tencent/TencentKona-11)
直接下载之后在本地解压即可。
![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211207212049.png)


2. 在服务器上安装各种依赖
其实没有什么技巧，首先安装
```xml
yum groupinstall "Development Tools"
```

然后在源目录下执行
```xml
bash configure
```

![](https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20211207212438.png)

然后一直根据提示安装各种依赖就行了。

3. 执行 `make images` 即可


# 使用
首先在IDEA中，点击`File->Project Structure`，删除现有的SDKs
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203004.png height=500>

然后重新添加我们目录下的SDK
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203150.png height=500>
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203248.png height=500>

此时源码还是只读的，无法进行更改，这是因为这里的源码我们还是默认去系统`$JAVA_HOME`目录下的src里面找的Java源代码，因为我们还需要将Sourcepath里面的src文件的目录进行修改：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203445.png height=500>
点击加号之后添加我们自己的openjdk目录：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203537.png height = 500>
添加之后确认：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809203646.png height = 500>


此时我们就可以进入源码进行修改了，例如最基础的`System.out.println()`，我们可以进入查看：
<img src=https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20210809204047.png height=500>

修改之后对应文件目录下的src文件就会被更改了，然后重新在源文件目录下`make images`就可以更新了
注意：`make images`会在原来的基础上增量编译，不会重新编译了
