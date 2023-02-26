---
title: Linux实操  
date: 2022-12-04 02:38:58  
tags: []  
categories:
  - 计算机
  - Linux
  - Command
---
# 一、登录

## 虚拟机网络的三种连接方式

1. 桥接模式 

   如图中张三所示，centos虚拟机的地址和张三共享了同一个网段。

    <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404043439.png" style="zoom:50%;" />

   **好处** 

   大家都在同一个网段，可以相互通讯 

   **缺点** 

   因为IP地址有限，可能造成ip冲突 

2. NAT模式【网络地址转换模式】 

   在windows基础上会**生成一个新的地址**，该地址不和原来的地址在一个网段中。 

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404043646.png" style="zoom:50%;" />

   **好处** 

   虚拟机不再占用原来的网段 

   **缺点** 

   该虚拟机不能被除了本机以外的其他机器找到，但是反过来该虚拟机可以找到其他机器（通过代理）。 

3. 主机模式 

   单独的一台电脑 

## linux目录结构

### 基本介绍 

linux的文件系统是采用级层式的树状目录结构。在此结构中最上层的根目录是"/"，然后在此目录下再创建其他目录。 

 在**Linux**世界中，**一切皆文件**

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404043829.png" style="zoom:50%;" />

### 目录结构

1. linux的目录中有且只有一个根目录/ 
2. linux的各个目录存放的内容是规划好的，不用乱放文件 
3. linux是文件的形式管理我们的设备，因此linux系统，一切皆为文件。 
4. linux的各个文件目录下存放什么内容需要有一个认识。 

# 二、用户

## 关机&重启指令

1. shutdown 

   ```shell
   shutdown -h now：表示立即关机 
   shutdown -h 1：表示一分钟后关机 
   shutdown -r now： 表示立即重启 
   ```

2. halt 

   直接使用，效果等价于关机 

3. reboot 

   就是重启系统 

4. sync 

   把内存的数据同步到磁盘上 

   **注意** 

   当我们关机或重启时，都应该先执行一下sync指令，把内存的数据写入磁盘，防止数据丢失。  

## 用户的登录和注销 

 ### 注销用户：logout 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404044234.png" style="zoom:50%;" />

#### 注意

logout在图形界面是无效的 

## 用户管理

### 基本介绍 

Linux系统是一个多用户多任务操作系统，任何一个要使用资源的用户，都必须首先向系统管理员申请一个账号，然后以这个账号的身份进入系统。 

### 1 添加用户 

基本语法 

```shell
useradd [选项] 用户名 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404044414.png" style="zoom:50%;" />

#### 注意 

1. 当用户创建成功后，会自动创建和用户同名的家目录 
2. 也可以通过useradd -d 指定目录 用户名 创建用户 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404045035.png" style="zoom:50%;" />

更改密码 

passwd 用户名 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404045056.png" style="zoom:50%;" />

### 2 删除用户 

基本语法 

```shell
userdel [选项] 用户名 
```

#### 应用案例

1. 删除小明，但是保留家目录

   方法：不带参数，直接删除

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406105833.png" style="zoom:50%;" />

2. 删除小明，同时删除家目录

   ```shell
   userdel -r xh
   ```

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406110015.png" style="zoom:50%;" />

在删除用户时，一般不会删除家目录



### 3 用户查询 

#### 基本语法 

```shell
id 用户名 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406110219.png" style="zoom:50%;" />

 

### 4 切换用户 

在linux中，如果当前用户权限不够，可以通过su -用户名的方式切换用户 

#### 基本语法 

```shell
su - 切换用户名 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406110259.png" style="zoom:50%;" />

#### 注意 

root切换到其他用户不用输密码，其他密码切换到root时需要输密码 

 

### 5 查看自己是什么用户 

#### 基本语法 

```shell
who am i 
```





## 组的管理

### 基本概念 

类似于角色，系统可以对**有共性的**多个用户进行统一的管理。 

### 1 创建组 

#### 基本语法 

```shell
groupadd 组名
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406110656.png" style="zoom:50%;" /> 

### 2 删除组 

#### 基本语法 

```shell 
groupdel 组名  
```

### 3 用户组 

增加用户时直接加上组 

#### 基本语法 

```shell
useradd -g 用户组 用户名 
```

### 4 修改用户组 

#### 基本语法 

```shell
usermod -g 用户组 用户名 
```



## 用户和组的相关文件

### /etc/passwd文件

用户（user）的配置文件，记录用户的各种信息

每行的含义：用户名:口令:用户标识号:组标识号:注释性描述:主目录:登陆Shell

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406111550.png" style="zoom: 33%;" />

### /etc/shadow文件

口令的配置文件

每行的含义：登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406111630.png)

### /etc/group文件

组（group）的配置文件，记录linxu包含的组的信息

每行含义：组名:口令:组标识号:组内用户列表

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406111652.png" style="zoom: 33%;" />

# 三、常用指令

## 1 指定运行级别

### linux共有7个运行级别 

0：关机 

1：单用户（找回丢失密码） 

2：多用户无网络服务 

3：多用户有网络服务（用的最多） 

4：保留级别 

5：图形界面 

6：重启 

### 系统运行级别配置文件 

/etc/inittab 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406171957.png)

### 切换到指定运行级别 

#### 基本语法 

```shell
init [012356] 
```

#### 应用 

通过init来切换不同的运行级别，比如5->3，然后关机 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406172145.png)

### 作用：通过运行级别找回丢失的密码 

如果不小心忘记了root密码如何找回？ 

#### 思路 

进入到单用户模式，然后修改root密码即可 

#### 原因 

进入单用户模式root不需要密码就可以登录 

#### 演示 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406172310.png" style="zoom: 33%;" />

输入e之后

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406172337.png" style="zoom:50%;" />

输入1：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406172352.png" style="zoom:50%;" />

#### 总结

开机->在引导时输入回车->看到一个界面输入e->看到一个新的界面，选中第二行（编辑内核） -> 再输入e -> 在这行最后输入1，再回车 -> 再次输入b，这时就会进入单用户模式。

此时我们就到了单用户模式，使用passwd指令来修改root密码。

#### 注意

该命令是不能通过远程命令找回root密码的，必须直接在服务器上进行操作。此时默认这台主机是你的。 

因此linux还是很安全的 

### 练习：如何让系统开机之后会自动进入命令行模式？ 

#### 思路 

将/etc/inittab中的默认运行级别改为3即可。 

## 2 软链接

### 基本语法 

```shell
ln -s 目标目录 软链接名 
```

### 示例 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406190626.png" style="zoom: 50%;" />



### 删除

```shell
rm -rf 软链接名
```

#### 注意

不能带斜杠

<img src="/Users/coachhe/Library/Application Support/typora-user-images/image-20210406190821577.png" alt="image-20210406190821577" style="zoom:50%;" />

## 3 查找类

### 1. find指令 

#### 基本语法 

```shell
find [搜索范围] [选项] 
```

选项说明如下案例 

#### 案例 

1. 找到home目录下的某个文件

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406191330.png" style="zoom:50%;" />

2. 查找home目录下属于某个用户的文件

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406191507.png" style="zoom:50%;" />

3. 查找某个范围内大小大于20M的文件

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406191617.png" style="zoom:50%;" />

### 2. locate指令

注意： 

第一次使用locate指令的时候，需要使用updatedb创建locate数据库 

# 四、权限

## 1. 组管理

### linux组的基本介绍 

linux的每个用户都必须属于一个组，不能独立于组外。 

在linux中每个文件在所有者、所在组、其他组的概念 

1. 所有者：属于哪个用户 
2. 所在组：属于哪个组 
3. 其他组：除去所在组之外的所有组 

#### 所有者 

一般来说，文件的创建者就是所有者 

##### 如何查看文件的所有者

```shell
ls -ahl
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406192043.png" style="zoom:50%;" />

##### 改变文件所有者

```shell
chown 用户名 文件名
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406192202.png" style="zoom:50%;" />

##### 注意

更改用户之后组不会改变

### 所在组

##### 修改文件所在组

```shell
chgrp 组名 文件名
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406192331.png" style="zoom:50%;" />

##### 改变用户所在组

```shell
usermod -g 组名 用户名
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406192450.png" style="zoom:50%;" />

## 2. 权限说明

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406192841.png" style="zoom: 33%;" />

### 文件类型

-：普通文件 

d：目录 

l：软链接 

c：字符设备（键盘、鼠标） 

b：块文件，硬盘 

### 文件权限

r：读 

w：写 

x：在文件上表示可以被执行，在目录上表示可以进入 

r、w、x的数字表示 

r表示4，w表示2，x表示1，因此rwx=4+2+1=7 

### 案例 

```shell
-rwxrw-r-- 1 root root 1213 Feb 2 09:39 abc 
```

#### 解析 

-表示这是个普通文件 

接下来三个rwx表示这个文件可以被所有者读写，也可以被执行 

接下来三个rw-表示这个文件可以组内的用户读写，但不能执行 

接下来三个r--表示可以被其他用户读，但不能写和执行 

1是硬链接数 

1213表示文件大小（如果是目录则为4096，目录本身占有的空间） 

## 3. 权限管理

### 修改权限 

用chmod修改文件权限 

#### 方法1：用+、-、=变更权限 

u：所有者 g：所有组 o：其他人 a：所有人(u,g,o的总和） 

1. chmod u=rwx,g=rx,o=x 文件目录名 
2. chmod o+w 文件目录名---------------->给其他人加上w权限 
3. chmod a-x 文件目录名------------------>给所有人减去x权限 

**举例**

新建一个abc文件，给所有者读写执行权限，给所在组读和执行的权限，给其他人读和执行的权限 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211202.png" style="zoom:50%;" />

去掉所有者的执行权限，给所在组加上写的权限：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211236.png" style="zoom:50%;" />

给文件的所有用户加上读的权限

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211323.png" style="zoom:50%;" />

#### 方法2：用数字方式实现 

用数字形式让文件权限修改为rwxr--r-x 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211436.png" style="zoom:50%;" />

4+2+1=7 

4=4 

4+1=5 

因此输入745 

### 修改文件所有者-chown 

#### 针对文件 

```shell
chown newowner:newgroup file 
```

或者 

```shell
chown newownver file 
```

#### 针对目录

如果是目录，则使用-R，使其下所有子文件或者目录递归生效 

#### 实例 

将文件abc所有者改为coachhe 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211623.png" style="zoom:50%;" />

其实相当于没操作 

将/home/目录下的目录abc以及里面内容所有者都改为jy 

注意：要使用root用户 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211650.png" style="zoom:50%;" />

### 修改文件所在组-chgrp 

#### 针对文件

则直接 

```shell
chgrp newgroup file 
```

或者 

```shell
chgrp newgroup file 
```

#### 针对目录

则 

```shell
chgro -R newgroup file 
```

#### 案例 

将文件abc的组更改为coachhe组 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406211821.png" style="zoom:50%;" />

## 权限实践---警察小偷游戏

有两个组：警察（police）和土匪（bandit） 

有两个警察：jack和jerry 

有两个土匪：xh和xq 

1. jack创建一个文件，自己可以读写，本组人可以读，其他组没有人有任何权限

   ```shell
   >>> touch hello 
   >>> chmod 640 hello 
   ```

2. jack修改该文件，让其他组人可以读，本组人可以读写 

   ```shell
   >>> chomd 646 hello 
   ```

3. xh投靠警察，看看是否可以读写。 

   ```shell
   >>> usermod -g police xh 
   >>> su - xh	## 注意：如果是在xh的界面，则需要注销之后再重新进入
   >>> vim abc 
   ```

   是可以读写的

# 五、任务调度

```shell
crontab 
-e 直接进行任务设置 
-l 显示所有任务 
```

# 六、分区和硬盘

## 1. 分区的概念

### 分区的方式 

1. mbr分区 
   - 最多支持四个主分区 
   - 系统只能安装在主分区 
   - 扩展分区要占一个主分区 
   - MBR最大只支持2TB，但拥有最好的兼容性 
2. gtp分区 
   - 支持无限多个主分区（但是操作系统可能限制） 
   - 最大支持18EB的大容量（1EB=1024PB，1PB=1024TB） 
   - windows7 64位以后支持分区 

## 2. linux分区原理示意图

1. Linux来说无论有几个分区，分给哪一个目录使用，它归根结底就只有一个根目录，一个独立且唯一的文件结构，linux中的每个分区都是用来组成整个文件系统的一部分。 

2. Linux采用了一种叫“载入”的处理方法，它的整个文件系统中包含了一整套的文件和目录，且将一个分区和一个目录联系起来。这时要载入的一个分区将使它的存储空间在同一个目录下获得。 

3. 示意图

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406212421.png" style="zoom: 50%;" />

## 3 硬盘说明

### 几大基本硬盘 

1. Linux硬盘分为IDE硬盘、virtio硬盘和SCSI硬盘，目前基本是SCSI硬盘（但是阿里云服务器的硬盘是virtio硬盘） 
2. 对于IDE硬盘，驱动器标识符为“hdx~”，其中“hd”表明分区所在设备的类型，这里是指IDE硬盘。“x”为盘号（a为基本盘，b为基本从属盘，c为辅助主盘，d为辅助从属盘），“~”表示分区，前4个分区用数字1到4表示，它们是主分区或者扩展分区，从5开始就是逻辑分区。 
3. 对于SISI硬盘则表示为“sdx~”，SCSI硬盘是用“sd”来表示分区所在设备的类型的，其余则和IDE硬盘的表示方式一样。 
4. 对于virtio硬盘，驱动器标识符为“vdx!”。其中“vd”表示分区所在设备的的类型，其余则和IDE硬盘的表示方式一样。 

### 比较 

整体上来看这三者的最大不同还是挂载磁盘的数量。根据在ovirt的上测试，一台win7的虚拟机，最多可以创建3个ide硬盘，当再次创建新的ide硬盘时候，会提示无法创建。同时，实验中最多创建了14块virtio硬盘，虽然界面允许创建更多virtio硬盘，但是超过14块，虚拟机是无法启动的。virtio-scsi可以创建足够多的硬盘，以至于协议自身并不是限制因素，而内核的限制反而成了硬盘数目的极限。 

### 查看系统分区和挂载的情况

```shell
lsblk -f   ## 查看系统分区和挂载的情况 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406212630.png" style="zoom:50%;" />

注意： 

挂载点就是文件系统 

 

### 查看系统分区和挂载的情况以及文件的大小情况 

```shell
lsblk 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406212704.png" style="zoom:50%;" />



## 4 linux挂载经典案例

### 需求 

给我们的linux系统增加一个新的硬盘，并且挂载到/home/newdisk 

### 如何增加一块硬盘 

1. 虚拟机添加硬盘 
2. 分区 
3. 格式化 
4. 挂载 
5. 设置可以自动挂载 

### 我的虚拟机和硬盘 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406212855.png" style="zoom:50%;" />

sda硬盘，若加上一块新硬盘应该为sdb。 

### 操作 

#### 1. 虚拟机添加硬盘 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406212937.png)

然后选择2G的硬盘，一路确定下去就可以了。 

注意：需要重启之后才能看到新的硬盘 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213003.png" style="zoom:50%;" />

#### 2. 分区 

```shell
fdisk /dev/sdb 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213045.png" style="zoom:50%;" />

得到结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213113.png" style="zoom:50%;" />

#### 3. 格式化 

```shell
mkfs -t ext4 /dev/sdb1 
```

含义是用mkfs -t指令将/dev/sdb1硬盘格式化为ext4格式。 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213148.png" style="zoom:50%;" />

结果 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213207.png" style="zoom:50%;" />

出现了文件类型和uuid，格式化成功 

#### 4. 挂载 

1. 先创建一个目录 

   ```shell
   mkdir /home/newdisk
   ```

2. 挂载mount

   ```shell
   mount /dev/sdb1 /home/newdisk 
   ```

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213330.png" style="zoom:50%;" />

   注意：

   ​	此时为临时挂载，重启之后就失去作用了。因此需要设置永久挂载 

#### 5. 设置可自动挂载 

修改/etc/fstab文件 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213446.png)

然后运行mount -a即可生效 

## 5 硬盘卸载

```shell
umount 设备名 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213525.png)

## 6 磁盘情况查询

### 查询系统整体磁盘使用情况 

#### 基本语法 

```shell
df -h /目录
```

查询指定目录的磁盘占用情况，默认为当前目录。 

#### 应用实例 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Docker/20210406213625.png" style="zoom:50%;" />

### 查询指定目录的磁盘占用情况 

#### 基本语法 

```shell
du -参数 /目录 
```

参数 

-a 指定目录 

-h 带计量单位 

--max-depth=1 子目录深度（也就是需要查到第几级） 

-s 指定目录占用大小汇总 

-c：列出明细的同时，增加汇总值 

#### 举例

查看/opt目录占用磁盘空间的情况，深度为1 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210406233939.png" style="zoom:50%;" />

注意： 

深度就是表示会显示/opt目录下的第几级文件，如果是1那就只可能显示/opt/rh而不会更具体到/opt/rh/xxx 

## 7 常用指令

### 1. 统计/home文件夹下文件的数量

```shell
ls -l | grep "^-" | wc -l
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210406234421.png" style="zoom:50%;" />

### 2. 统计/home文件夹下目录的数量

```shell
ls -l | grep "^d" | wc -l
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210406234511.png" style="zoom:50%;" />

### 3. 统计/home文件夹下文件的个数，包括子文件夹里面的

```shell
ls -lR /home | grep "^-" | wc -l
```

### 4. 统计/home文件夹下文件夹的个数，包括子文件夹里面的

```shell
ls -lR /home | grep "^d" | wc -l
```

### 5. 以树状形式展示整个目录

```shell
tree
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210406234737.png" style="zoom:50%;" />

# 七 网络配置

## 1 linux网络配置原理图

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210406235842.png)

### 以我的电脑为例

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407000655.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407000802.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407000728.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407000745.png" style="zoom:50%;" />

## 2 查看网络ip和网关

### 1. 查看虚拟网络编辑器

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407000959.png"  />

### 2. 修改ip地址 

点击上面的修改即可以 

### 3. 查看网关 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001036.png)

## 3 linux网络环境配置

### 1. 自动获取

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001128.png)

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001140.png" style="zoom:50%;" />

点上连接即可。 

**缺点 ：**

每次自动获取的IP地址不相同。因此不适合做服务器，因为服务器ip是固定的。 

### 2. 指定固定ip地址

#### 说明 

直接修改配置文件来指定IP，并可以接到外网，编辑/etc/sysconfig/network-scripts/ifcfg-etho 

#### 要求 

将ip地址配置的是静态的 

#### 方法 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001251.png)

## 4 修改主机名

### 1. 查看当前主机名 

```shell
echo $HOSTNAME 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001354.png" style="zoom:50%;" />

### 2. 修改linux的主机映射文件 

```shell
vim /etc/sysconfig/network 
```

中的内容 

```txt
NETWORKING=yes 
NETWORKING_IPV^=no 
HOSTNAME=xxxxx//新的主机名 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001455.png" style="zoom:50%;" />

注意：主机名不能有“_”下划线 

### 3. 修改/etc/hosts增加ip和主机之间的映射

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001534.png" style="zoom:50%;" />

### 4. 重启设备

# 八 进程管理

## 1 进程的基本介绍

1. 在Linux中，每个执行的**程序（代码）**都称为一个**进程**，每个进程都分配一个ID号 
2. 每一个进程，都会对应一个父进程，而这个父进程可以复制多个子进程，例如www服务器。 
3. 每个进程都可能以两种方式存在。前台和后台，所谓前台进程就是用户目前的屏幕上可以进行操作的。后台进程则是实际在操作，但由于屏幕上无法看到的进程，通常使用后台方式执行。 
4. 一般系统的服务都是以后台进程的方式存在，而且都会常驻在系统中，直到关机才结束。 

## 2 显示系统执行的进程

### ps指令 

```shell
ps 
```

#### 说明 

查看执行的指令是ps，一般来说使用的参数是ps -aux 

```shell
ps -a  ## 显示当前终端的所有进程信息 

ps -u  ## 以用户的格式显示进程信息 

ps -x  ## 显示后台进程进行的参数 
```

##### 举例 

```shell
ps -aux | more 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407001859.png)

#### 显示专门的进程

显示专门的进程可以用grep过滤

##### 举例

我想看有没有关于sshd相关的进程： 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002036.png)

#### 看某个进程的父进程 

##### 举例

```shell
ps -ef | more 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002141.png)

## 3 终止进程kill和killall

### 基本语法 

```shell
kill 选项 进程号 ## 功能描述：通过进程号杀死进程
killall 进程名称 ## 功能描述：通过进程名杀死进程，也支持通配符，这在系统因负载过大而变得很慢时很有用 
```

### 常用选项 

-9：强迫进程立即停止 

### 案例 

#### 1. 踢掉某个非法登录的用户 

这里存在某个illegal_user: 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002358.png" style="zoom:50%;" />

我想把他踢走，因为他是通过sshd远程登录，因此可以查到登录信息 

```shell
ps -aux | grep sshd 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002431.png" style="zoom:50%;" />

可以看到有illegal_user的登录信息。 

kill掉这个进程即可 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002458.png)

#### 2. 终止sshd服务的操作 

```shell
kill 1655 
```

导致的结果就是不能再使用远程进行连接了。 

#### 3. 终止多个gedit编辑器（通过killall） 

通过多个终端打开了多个gedit编辑器 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002609.png)

使用命令： 

```shell
killall gedit
```

 ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002643.png)

两个终端的gedit程序都已经同时被杀死。 

#### 4. 强制杀掉一个终端 

首先查看当前打开的所有终端（bash） 

```shell
ps -aux | grep bash
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002752.png)

在这里，左边的bash对应右边的终端 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002810.png" style="zoom:50%;" />

首先kill：

```shell
kill 3103
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002904.png" style="zoom:50%;" />

结果：

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002923.png" style="zoom:50%;" />

但是此时发现右边依旧存在。 

因此用-9，强制终止。 

```shell
kill -9 3103
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407002957.png" style="zoom:50%;" />

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003014.png" style="zoom:50%;" />

## 4 linux服务管理（service）简介和原理

### 服务介绍 

服务（service）本质就是进程，但是是运行在后台的，通常都会监听某个端口，等待其他程序的请求，比如mysql，sshd，防火墙等，因此我们又称为守护进程，是Linux中非常重要的知识点 

### 服务原理图 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003136.png)

也就是说，服务本身就是监控某个端口的进程，用来提供ssh或者远程连接mysql等服务。

### service管理指令

```shell
service 服务名 [start | stop | restart | reload | status] 
```

#### 注意 

在Centos7.0之后不再使用service，而是用systemctl，但用法几乎相同 

### 案例 

查看当前防火墙的状况，关闭防火墙和重启防火墙。 

#### 1. 查看防火墙当前状态 

```shell
service iptables status 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003342.png)

#### 2. 关闭防火墙和重启防火墙 

```shell
service iptables stop 
service iptables start 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003424.png)

## 5 telnet指令 

通过telnet指令检查linux的某个端口是否在监听，并且可以访问 

### 指令 

```shell
telnet ip 端口 
```

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003538.png" style="zoom:50%;" />

可以看到，建立起了连接，这意味着22号端口是在服务的。 

### 注意：永久生效问题 

service只是用来临时生效的，重启系统之后，还是回归以前对服务的设置。 

如果希望设置某个服务自启动或关闭永久生效，要使用chkconfig指令。 

## 6 chkconfig指令

### chkconfig指令

通过chkconfig命令可以给每个服务的各个运行级别设置自启动/关闭 

### 基本语法 

#### 1. 查看服务： 

```shell
chkconfig --list|grep xxx ## 或者 
chkconfig xxx --list 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003804.png)

可以查看sshd指令在每个运行级别的自启动状态 

#### 2. 设置某个服务在某个运行级别下是否自启动 

```shell
chkconfig --level 级别 xxx on/off 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407003907.png)

可以看到，将sshd在运行级别为5时的自启动关闭后再重新打开了。  

### 注意 

chkconfig设置之后需要重启之后才能生效。 

# 九 进程监控

## 1 动态监控进程top 

### top介绍 

top与ps很相似，它们都用来显示正在执行的进程，区别在于top会实时更新 

### 基本语法 

top [选项] 

### 案例 

#### 1. top直接使用的案例 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407004153.png)

#### 2. 查看某个用户的案例 

输入u之后出现下面这行： 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407004227.png)

然后输入root回车即可 

#### 3. 杀死某个进程 

输入k之后出现下面这行 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407004310.png)

然后输入进程号回车即可 

#### 4. 指定系统状态更新的时间 

```shell
top -d time 
```

#### 5. 互动指令 

```shell
P:CPU使用率 
M:按内存排序 
N:按PID排序 
```

## 2 监控网络状态netstat

### 基本语法 

```shell
netstat [选项] 
```

### 选项说明 

一般来说都是netstat -anp 

-an：按一定顺序排列输出 

-p：显示哪个进程在调用 

###  应用案例 

#### 1. 查看系统所有的网络服务 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407004741.png)

### 2. 查看系统的某个网络服务

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407004817.png)

# 十 下载管理

## 1 rpm包的管理

### 简介 

RPM是RedHat Package Manager（RehHat软件包管理工具）的缩写，这一文件名称虽然打上了RedHat的标志，但理念是通用的。 

### rpm包的简单查询指令 

查询已安装的rpm列表 

#### 基本语法 

```shell
rpm -qa | grep xx 
```

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005004.png)

#### 注意 

i686、i386表示32位系统，noarch表示通用 

### rpm包其他查询指令 

```shell
rpm -qa 
rpm -qa 查询所有rpm软件包 
rpm -qa | more 查询所有，以分页显示 
rpm -qa | grep xxx 查询某个 
```

#### rpm -qi xxx 

rpm -qi xxx 查询安装的rpm包软件的信息 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005114.png)

#### rpm -ql xxx 

查询软件包中的文件 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005236.png)

#### rpm -qf 文件路径名 

查询文件所属的软件包 

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005256.png" style="zoom:50%;" />

## 2 rpm包的卸载和安装

### 1. 删除

#### 基本语法 

```shell
rpm -e RPM包的名字 
```

#### 注意 

如果删除其他软件包依赖于要卸载的软件包，卸载时则会报错： 

若需要强制删除（不推荐）则运行： 

```shell
rmp -e --nodeps xxx 
```

### 2. 安装 

#### 基本语法 

rpm -ivh RPM包全路径名称 

#### 参数说明 

i=install 安装 

v=verbose 提示 

h=hash 进度条 

#### 应用实例：安装火狐浏览器 

1. 找到firefox安装包，需要挂载上我们安装centos的iso文件，然后到/media/下去找 

   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005619.png)

2. 将其复制到某个希望的文件夹后安装

   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005656.png)

## 3 yum介绍

### 介绍 

yum是一个Shell前端软件管理器。**基于RPM包管理****，能够从指定的服务器自动下载RPM包并且安装，可以**自动处理依赖关系**，并且一次性安装所有依赖的软件。 

使用yum的前提是可以联网 

### 指令 

```shell
yum list 
```

用来看所有yum服务器上的软件 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005821.png)

```shell
yum list | grep xxx 
```

用来看所需要的软件在yum服务器上是否存在，若存在可以直接安装，否则需要另想办法 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005848.png)

```shell
yum install xxx 
```

直接使用 

### 用yum来安装chrome

#### 查看yum服务器是否有chrome 

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210407005934.png)

#### 2. yum install chrome即可