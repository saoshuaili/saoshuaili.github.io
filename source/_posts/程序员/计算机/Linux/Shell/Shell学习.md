---
title: Shell学习  
date: 2022-12-04 02:39:04  
tags: []  
categories:
  - 计算机
  - Linux
  - Shell
---

### 1 基本概念

## 双括号结构

### 特点
1. 在双括号结构中，所有表达式可以像C语言一样，如a++，b--等
2. 在双括号结构中，所有变量可以不加入："$"符号前缀
3. 双括号可以进行逻辑运算，四则运算
4. 双括号结构扩展了for，while，if条件测试运算
5. 支持多个表达式运算，各个表达式之间用逗号分开


### 例：依次输出100以内的2的幂值
#### while方式
![](https://pic.downk.cc/item/5fc7496a394ac5237899b675.png)
#### 双括号方式
![](https://pic.downk.cc/item/5fc7496b394ac5237899b680.png)

## 引号

### 单引号和双引号的区别

单引号之间的内容原封不动地指定给了变量(也就是说，对立面的内容不做任何更改，输入什么就是什么，不做操作）
双引号取消了空格的作用，特殊符号的含义保留

### 反引号与$()
两个方法都可以将一条命令或者某个脚本的执行结果赋给变量。

### 方法1:用反引号将命令括起来
格式为：
```
变量=`命令` 
```
### 方法2:用美元符和小括号将命令括起来，
格式为：
```
变量=$(命令) 
```
例如，将命令date 的运行结果赋给变量A 和B：
```
A=`date`  
echo $A  
```
结果：Sat Apr 16 22:51:26 CST 2011 
变量A 存放了date 命令的执行结果  
```
B=$(date)  
echo $B  
```
Sat Apr 16 22:57 :47 CST 2011 
变量B 存放了date 命令的执行结果 

## 运行路径

```shell
#!bin/bash 
```
井号(#)加感叹号(!)用作shell命令的完全路径：  
显示后期命令以哪种shell来执行

### 注意：
shell有很多种  
![](https://pic.downk.cc/item/5fc7496b394ac5237899b68f.jpg)
最常用的就是bin目录下的bash（mac上较常用的是zsh）

## 注释
```shell
# This is what a example looks like
```
这是注释，执行时被忽略


## 文件名
shell程序一般以.sh作为结尾

## 创建shell程序的步骤
1. 创建包含命令和控制文件的shell文件
2. 修改这个文件的权限使他可运行
```shell
chmod u+x
```
3. 执行
./example.sh或者绝对路径（例如root/test/example.sh)

# 2 变量

## $?

### 作用
$?表示上一条指令返回的结果：若正常结束则为0，非正常结束则为1

### 举例

例如指令

```shell
ls
```
那么一定返回0（正常结束）,因为ls不存在非正常返回
若
```
ls a.txt
```
并且a.txt不存在的话，则返回1.

![](https://pic.downk.cc/item/5fc74b84394ac523789ac9e2.jpg)

## shift使用

#shift命令:参数左移指令
每执行一次，参数序列顺次左移一个位置，$#的值减1，用于分别处理每个参数，移动出去的参数，不能再用

![](https://pic.downk.cc/item/5fc74a0d394ac523789a3292.jpg)

## 临时变量和永久变量

### 临时变量
临时变量是shell内部定义的，使用范围仅限于定义它的程序，对其他程序不可见。例如用户自定义变量，
#### 用户自定义变量
由字母或下划线大头。由字母、数字和下划线组成，并且大小写字母意义不同，变量名长度没有限制。**使用**变量值时，要在变量名前加上前缀"$",通过=进行变量的**赋值**。
##### 注意：
赋值时两边不能有空格，可以将表达式的结果赋值给变量。
例如：
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d11.jpg)  
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d21.jpg)
##### 列出所有变量
使用set命令：  
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d37.jpg) 
内容非常多

##### 查看某个变量的值
用set | grep XX 命令  
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d46.jpg)

##### 删除变量
unset命令
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d57.jpg)

### 永久变量
是环境变量，其值不随shell脚本的执行结束而消失  
##### 例如：
![](https://pic.downk.cc/item/5fc74abf394ac523789a7d60.jpg)

## 位置变量和特殊变量
### 1. 位置变量
Shell解释执行用户的命令时，将命令行的第一个字作为命令名，而其他字作为参数。由出现在命令行上的位置确定的参数称为位置参数。
位置变量：使用$N来表示
例：
./example.sh file1 file2 file3
$0：这个程序的文件名example.sh
$n：这个程序的第n个参数，n=1..N
### 2. 特殊变量
有些变量时一开始执行Script脚本时就会设定，且不能被修改，但我们不叫它只读的系统变量，而叫他特殊变量。这些变量当一个执行时就有了，以下是一些特殊变量：
\$* 程序的所有参数
\$# 这个程序的参数个数
$$ 这个程序的PID
\$! 执行上一个后台程序的PID
\$? 执行上一个指令的返回值
\$_ 在此之前执行的命令或者脚本的最后一个参数
#### 特殊变量举例
![](https://pic.downk.cc/item/5fc74b84394ac523789ac9e9.jpg)
#### 举例：变量在shell中的使用
![](https://pic.downk.cc/item/5fc74b84394ac523789ac9f5.jpg)

# 3 语法



## test测试语句


### 测试字符串
```
test str1==str2 是否相等
test str1!=str2 是否不相等
test str1 测试字符串是否不空
test -n str1 
test -z str1
```

### 测试正数：
```shell
test ini1 -eq ini2   测试是否相等
test int1 -ge int2   测试int1是否大于等于（ge，greater or equal)int2
test int1 -gt int2   测试int1是否大于int2
test int1 -le int2   测试int1是否小于等于int2
test int1 -lt int2   测试int1是否小于int2
test int1 -ne int2   测试int1是否不等于int2
```
注意：
也可以省略test

### 文件测试：
```shell
test -d file # 测试是否为目录
test -f file # 测试是否为文件
test -x file # 测试是否可执行
test -r file # 测试是否可读
test -w file # 测试是否可写
test -e file # 测试文件是否存在
test -s file # 测试大小是否为空，是否为空文件
```

## 算术表达式

### 作用
对整数型变量进行算术运算
##### 注意
运算符之间要有空格  
![](https://pic.downk.cc/item/5fc74c09394ac523789b0426.jpg)

### 举例
![](https://pic.downk.cc/item/5fc74c09394ac523789b0430.jpg)
#### 注意：
1.在这里是用的是反引号，不是单引号或者双引号，这样才能使用的是计算出来的结果
2.expr中间的所有符号都必须有空格

### 反引号和$()的区别
反引号是使用后面表达式计算出来的结果  
$()表示的是整个表达式

## 条件语句

### case语句

流控制语句 适用于多分支
#### 格式
![](https://pic.downk.cc/item/5fc74c91394ac523789b4147.jpg) 
最后的esac是case的倒过来写

### if控制语句
#### if语法
```shell
if 条件 ;then
语句
fi
```
#### 举例
![](https://pic.downk.cc/item/5fc74c91394ac523789b4156.jpg)
#### if/else语法
```shell
if 条件; then
语句1
else
语句2
fi
```

![](https://pic.downk.cc/item/5fc74c91394ac523789b416d.jpg)

#### 多条件联合
-a 或 && :逻辑与  
-o 或 ||：逻辑或

更复杂的if语句：
```shell
if 条件1 ; then
   语句1
elif 条件2 ; then
   语句2
else 
   语句3
fi
```

#### 举例：判断文件类型
```shell
#!/bin/bash
#
if [ $# -lt 1 ];then
    echo "A argument is needed."
    exit 1
fi

if ! [ -e $1 ];then
    echo "No such file or directory."
    exit 2
fi

if [ -f $1 ];then
    echo "Common file"
elif [ -d $1 ];then
    echo "Directory"
elif [ -L $1 ];then
    echo "Symbolic link"
elif [ -b $1 ];then
    echo "block special file"
elif [  -c $1 ];then
    echo "character special file"
elif [  -S $1 ];then
    echo "Socket file"
else
    echo "Unknow"
fi
```

## 循环语句

### continue和break

break+continue用法和其他语言差不多：
#### 实例
![](https://pic.downk.cc/item/5fc74d78394ac523789ba123.jpg)
#### 输出
![](https://pic.downk.cc/item/5fc74d78394ac523789ba131.jpg)

### for...done格式
#### 语法
```shell
for 变量 in 变量群
do
   xxx
done
```
![](https://pic.downk.cc/item/5fc74d78394ac523789ba141.jpg)

### while循环
![](https://pic.downk.cc/item/5fc74d78394ac523789ba154.jpg)

## 4 文件类型

-z：是否是合格文件  
-e：文件是否存在  
-f：是否是一个普通文件  
-L：是否是一个软链接文件  
-b：是否是一个块设备文件  
-c：是否是一个字符设备文件  
-p：是否是一个管道文件  
-S：是否是一个套接字文件  
-s：判断内容是否为空



```shell
if [ -z $filename ]
then
    echo -e "错误，请输入文件！"
    exit 222
fi
if [ ! -e $filename ]
then
    echo "你输入的文件不存在"
fi
if [ -f $filename ]
then
    echo "你输入的文件存在；并且是一个普通文件"
fi
if [ -d $filename ]
then
    echo -e "你输入的文件存在；并且是一个目录"
fi
if [ -L $filename ]
then
    echo -e "你输入的文件存在；并且是一个软链接文件" 
fi
if [ -b $filename ]
then
    echo -e "你输入的文件存在；并且是一个块设备文件"
fi
if [ -c $filename ]
then
    echo -e "你输入的文件存在；并且是一个字符设备文件"
fi
if [ -p $filename ]
then
    echo -e "你输入的文件存在；并且是一个管道文件"
fi
if [ -S $filename ]
then
    echo -e "你输入的文件存在；并且是一个套接字文件"
fi
```

# 进阶

## -MURI Escape, &_

```
a=$(echo "$url" | perl -MURI::Escape -ne 'chomp;print uri_escape($_),"\n"')
```

-MURI::Escape是URL模块。
该模块导出两个函数：
uri_escape:该函数执行URL转义，也就是URL编码，将网址编码
uri_unescape:该函数执行URL转义，也就是URL解码，将编码得到的网址进行解码

$_ 表示在此之前执行的命令或者脚本的最后一个参数

```
echo “$url" | perl -MURI::Escape -ne 'chomp;print uri_escape($_),"\n"'
```
作用：
将URL中的保留字符转换为其百分比编码的对应字符。

假设定义了一个变量为file：
代码如下:
file=/dir1/dir2/dir3/my.file.txt
可以用${ }分别替换得到不同的值：

### ${file#*/}
作用：删掉第一个 / 及其左边的字符串：
结果：dir1/dir2/dir3/my.file.txt

### ${file##*/}
作用：删掉最后一个 /  及其左边的字符串
结果：my.file.txt

#### 举例
```shell
echo "--hello--" | sed 's/-//g'
```
在这里是将-全部去掉（因为g前面的两个/中间没有任何内容），若是sed 's/-/a/g'就是将所有-替换成a
### ${file#*.}
作用：删掉第一个.及其左边的字符串
结果：file.txt
### ${file##*.}
作用：删掉最后一个.及其左边的字符串
结果：txt
### ${file%/*}
作用：删掉最后一个/及其右边的字符串
结果：/dir1/dir2/dir3
### ${file%%/*}
作用：删掉第一个/及其右边的字符串
结果：(空值)
### ${file%.*}
作用：删掉最后一个.及其右边的字符串
结果：/dir1/dir2/dir3/my.file
### ${file%%.*}
作用：删掉第一个.及其右边的字符串
结果：/dir1/dir2/dir3/my


#记忆的方法为：
\#是去掉左边（键盘上#在$的左边）

\%是去掉右边（键盘上%在$的右边）

#匹配
单一符号是最小匹配；两个符号是最大匹配

## $, grep, /dev/null

```shell
echo $h|grep Authorization > /dev/null
```
/dev/null:
这是一个特殊设备，称为空设备，它会丢弃一切写入它的数据，但是读取它则会抛出错误。
在shell中常用它来表示放弃执行的结果，
这行的作用是值需要得到

```shell
echo $h | grep Autorization
```
的结果，而不需要将其输出，也就是看$h中是否存在Autorization，作为接下来的$?的输入，如果存在返回0，如果不存在则返回1.

## 得到最后一级目录名

```shell
last_dir=`echo $dir|awk -F'/' '{print $NF}'`
```















