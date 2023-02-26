---
title: Linux基本命令  
date: 2022-12-04 02:38:48  
tags: []  
categories:
  - 计算机
  - Linux
  - Command
---

# cat

### 普通cat
显示文本内容

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404012449.png" style="zoom:50%;" />

### cat -n 1.txt
-n参数给所有的行加上行号

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404012510.png" style="zoom:50%;" />


### cat -b 1.txt
对脚本很有用，只给有文本的行加上行号

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404012623.png" style="zoom:50%;" />


### cat结合wc显示行数
```
cat 1.txt | wc -l
```
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210404012656.png" style="zoom:50%;" />

显示1.txt的行数

### 参数
\- c 统计字节数。
\- l 统计行数。
\- w 统计字数。

# cp

### cp -i test_one test_two

询问是否用test_one替换test_two

### cp -R

在一条命令中递归地复制整个目录的内容

# crontab

用来编辑定时任务。

### 列出所有定时任务

```shell
crontab -l
```
得到所有crontab任务

### 编辑crontab文件

```c
crontab -e
```

# date

### date
```
date
```


### date +"%F"
```
date +"%F"
```


### date +"%F %T"
```
date +"%F %T"
```


### date -d
得到字符串形式的输出

### date +%s
得到当前unix时间戳

### date +%s -d"24 hour ago"
得到24小时之间的unix时间戳

# ls

### ls
展示所有文件和目录


### ls -F
区分文件和目录，是目录的后面会有"/"


### ls -a
将隐藏文件和普通文件及目录一起显示出来


### ls -R
递归显示当前目录下包含的子目录的文件


### ls -l
显示长列表

# man

### bash手册
man命令用来访问存储在Linux系统上的手册压面。在想要查找的工具的名称前面输入man命令，就可以找到那个工具相应的手册条目。

### Linux手册页的内容区域
| 区域号 | 所涵盖的内容             |
| ------ | ------------------------ |
| 1      | 可执行的程序或shell命令  |
| 2      | 系统调用                 |
| 3      | 库调用                   |
| 4      | 特殊文件                 |
| 5      | 文件格式与约定           |
| 6      | 游戏                     |
| 7      | 概览、约定及杂项         |
| 8      | 超级用户和系统管理员命令 |
| 9      | 内核例程                 |

# rm

### rm -i

删除前询问是否删除

### rmdir
rmdir是删除目录，没有rmdir -i，因为其只能删除空目录，可以先用rm -i进入其目录删除其中的文件，然后再删除目录本身

### rm -r / rm -rf
会直接将目录和其中的文件一起删除
和rm -rf相同

### rm -ri
会进行递归询问

# tr

### 定义

tr是对管道传输过来的参数进行过滤。tr是一个过滤器
##作用
对来自标准输入的字符进行替换、压缩和删除。它可以将一组字符变成另一组字符，经常用来编写优美的单行命令，作用很强大

### 举例
1. 将输入字符由大写转换为小写：
```
echo "hello world" | tr 'a-z' 'A-Z'
```
输出：
HELLO WORLD

2. 使用tr删除字符：
```
echo "hello 123 world 456" | tr -d '0-9'
```
输出：
hello world

3. 将制表符转换为空格
```
cat text | tr '\t' ' '
```


4. 字符集补集，从输入文本中将不在补集中的所有字符删除
```
echo aa.,a 1 b#$bb 2 c*cc 3 ddd 4 | tr -d -c '0-9 \n'
```
输出：  
1 2 3 4  
解释：  
在这里，-d和-c配合，如果去掉-d则会出错，如果去掉-c则会得到相反的结果，会将所有0-9删除。

# wc

wc用来统计字数
输出格式如下：
```shell
$ wc testfile           # testfile文件的统计信息  
3 92 598 testfile       # testfile文件的行数为3、单词数92、字节数598 
```
输出的第一个参数为行数，第二个参数为单词数量，第三个参数为字节数量，第四个参数是文件名

















