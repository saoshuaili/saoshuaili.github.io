---
title: Scala基础  
date: 2022-12-04 02:25:02  
tags: []  
categories:
  - 编程语言
  - Scala
---

# 一、Scala 简介
Spark1.6 使用的是 Scala2.10，Spark2.0 版本以上使用的是 Scala2.11

## Scala 6 大特性

- Java 和 scala 可以混编
   可以在 Java 中调用 Scala 的包和类，也可以在 Scala 中调用 Java 的包和类
- 类型推测 (自动推测类型)
   不需要指定 int 或 double 等，会自动进行类型的推断。
   **注意：**
   Scala 不是一种弱语言类型，只是类型会自动进行推断。
- 并发和分布式（Actor）
   在 Scala 编程中不需要考虑锁。
- 特质，特征 (类似 java 中 interfaces 和 abstract 结合)
    trait（特质）类似 interfaces 和 abstract 进行整合。
- 模式匹配（类似 java switch）
   java 中的 switch 中只能匹配值，例如 1，2 等，或者'a'，'b'，而不能匹配 String，例如"aba"是不能放在 switch 中的 case 中的，而 Scala 可以。
- **高阶函数**
   **至关重要**，在方法中传递方法。

## Scala 安装使用（MAC 版）

1. 下载 scala 安装包

   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Flink/20201215144723.png)
2. 将解压之后的文件移动到/usr/local/share 中
   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201215145005.png" style="zoom:50%;" />
3. 将 Scala 的 bin 目录加到~/. bash_profile 的最后一行
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201215145512.png)
4. 别忘了 source 一下
   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201215150943.png" style="zoom:50%;" />
5. 最后运行一下 scala，证明已经成功安装。
   ![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201215151450.png)
   
   注意：
   最好还是在 linux 上安装和运行 scala，自己的 mac 也太折腾了...
   
## IDE 和插件

IDE 选择 IDEA。
插件选择 Scala。

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201215151644.png" style="zoom:50%;" />

# 二、Scala 基础
## 数据类型

| **数据类型** | **描述**                                                     |
| ------------ | :----------------------------------------------------------- |
| Byte         | 8位有符号补码整数。数值区间为 -128 到 127                    |
| Short        | 16位有符号补码整数。数值区间为 -32768 到 32767               |
| Int          | 32位有符号补码整数。数值区间为 -2147483648 到 2147483647     |
| Long         | 64位有符号补码整数。数值区间为 -9223372036854775808 到 9223372036854775807 |
| Float        | 32 位, IEEE 754 标准的单精度浮点数                           |
| Double       | 64 位 IEEE 754 标准的双精度浮点数                            |
| Char         | 16位无符号Unicode字符, 区间值为 U+0000 到 U+FFFF             |
| String       | 字符序列                                                     |
| Boolean      | true或false                                                  |
| Unit         | 表示无值，和其他语言中void等同。用作不返回任何结果的方法的结果类型。Unit只有一个实例值，写成()。 |
| Null         | null 或空引用                                                |
| Nothing      | Nothing类型在Scala的类层级的最底端；它是任何其他类型的子类型。 |
| Any          | Any是所有其他类的超类                                        |
| AnyRef       | AnyRef类是Scala里所有引用类(reference class)的基类           |

## 介绍

- Byte，Short，Int，Long，Float，Double，Char，String 和 Boolean 和 Java 都一样，只是首字母大写了。
- Unit = void
- Null = null
- Nothing 是所有类型的子类型，处于 Scala 的类层级的最低端。
<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201216192436.png" style="zoom:50%;" />	
- Any 是所有类型的超类，任何实例都属于 Any 类型，类似 Java 中的 Object
- AnyRef 是所有引用类型的超类
- AnyVal 是所有值类型的超类，例如 Int 类

### 几个容易混淆的概念

| 类型     | 介绍                                                         |
| :------- | ------------------------------------------------------------ |
| Null     | Trait，其唯一实例是 null，是 AnyRef 的子类，不是 AnyVal 的子类    |
| Nothing  | Trait，所有类型的子类，没有实例                              |
| Unit     | 无返回值的函数类型，也就是 void                               |
| Nil      | 长度为 0 的 List                                                |
| **None** | **Option 的两个子类之一，另一个是 Some，用于安全的函数返回值** |

None 相当于 Java 中的 Map get 一个值但是不存在时的返回值。

## Scala 使用

### 1 新建一个项目

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201216173041.png)

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201216173655.png)

### 示例

![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201216174029.png)

## Scala 类和对象

- Scala object 相当于 Java 中的单例，object 中定义的全是静态的
- Scala 中定义变量使用 var，定义常量使用 val，变量可变常量不可变，变量和常量类型可以省略，会自动推断
- Scala 中每行后面都会有分号自动推断机制，不用显式写出";"
- 建议在 Scala 中命名使用驼峰命名法
- Scala 类中可以传参，传参一定要指定类型，有了参数就有了默认构造，但是 object 不能传参，若需要传参则需要定义 apply 方法
- 类中重写构造，构造第一行必须先调用默认的构造
- Scala 中当 new class 时，类中除了方法不执行（除了构造方法），其他的都执行
- Object 一定会被加载，而 class 只有 new 了之后才会被加载
- 在同一个文件中 Object 和 class 的名称可以一样，一样时 class 叫做 object 的伴生类，object 叫做 class 的伴生对象，可以互相访问私有变量

## 解释代码

```java
package com.coachhe.scala

object ClassAndObject {
  val price = 100
  println(price)

  //若object需要传参则需要定义这个方法
  def apply(i:Int)={
    println("Score is" + i)
  }

  def main(args: Array[String]): Unit = {
    val name = "coachhe"
    println(name)
    val p = new Person("zhangsan", 20, 'F')
    println(p.age)
    println(p.name)
    println(p.gender)

  }

}

// Java的类没有括号，不能传参，Scala可以
// 传递的参数xname和sage是Person私有的东西，不可见
class Person(xname :String, xage :Int){
  val name = xname
  val age = xage
  var gender = 'M'

  def sayName()={
    println("Hello World_" + ClassAndObject.price)
  }

  def this(yname :String, yage :Int, ygender: Char){
    this(yname, yage)
    this.gender = gender
  }

}

```

## if else 语句

和Java 一模一样。

## for 循环

- 1 to 10 就是 Range (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
- 1 until 10 就是 Range (1, 2, 3, 4, 5, 6, 7, 8, 9)
- 1 to (10, 2) 就是 Range (1, 3, 5, 7, 9)

## while 语法

和 java 完全相同

## 具体应用

```java

object Grammer {
  def main (args: Array[String]): Unit = {

    /**
     * if else
     */
    val age = 20;
    if (age < 20) {
      println ("age < 20")
    } else if (age == 20 ){
      println ("age = 20")
    } else {
      println ("age > 20")
    }

    /**
     * for循环
     * 注意：在这里
     * 1 to 10 就是Range(1,2,3,4,5,6,7,8,9,10)
     * 1 until 10 就是Range(1,2,3,4,5,6,7,8,9)
     * 1 to (10,2) 就是Range(1,3,5,7,9)
     */
    for (i <- 1 to 10) {
      print(i + " ")
    }
    println()
    for (i <- 1 to (10,2)) {
      print(i + " ")
    }
    println()
    for (i <- 1 to 3; j <- 1 to 3) {
      //在字符串之前加上s可以不用进行拼接
      println(s"i =  $i  j = $j")
    }

    /**
     * while语法
     * 和java完全相同
     */
    var i = 0;
    while (i < 3) {
      println(s"hello $i")
      i += 1
    }


  }

}

```

## 输出

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20201216191447.png" style="zoom:50%;" />


# 三、Scala方法与函数

## 方法定义

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210112204951.png" style="zoom:33%;" />

### 注意
- 方法体中最后返回值可以使用 return，如果使用 return，那么方法体的返回值类型一定要指定。
- 方法体返回值可以省略，默认将方法体中最后一行计算结果当做返回值返回
- 定义方法传入的参数一定要指定类型
- 方法体中内容如果一行可以写完，那么方法体的括号可以省略
- 如果定义方法时，省略了方法名称和方法体之间的"="，那么无论方法体最后一行计算的结果是什么，都会被丢弃，返回 Unit

### 结果

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210112205018.png" style="zoom:33%;" />

### 输出

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210112205056.png" style="zoom:50%;" />

## 注意

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108185937.png" style="zoom:33%;" />

## 递归方法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108194250.png" style="zoom:33%;" />

### 注意

递归时需要显式指定返回类型。

## 参数有默认值的方法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108205604.png" style="zoom:33%;" />

## 可变长参数的方法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108210353.png" style="zoom:33%;" />

## 匿名函数（匿名方法）

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108213019.png" style="zoom:33%;" />

## 嵌套方法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210108213236.png" style="zoom: 33%;" />

## 偏应用函数

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210110165346.png" style="zoom:33%;" />



## 高阶函数

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210110170624.png" style="zoom:33%;" />

### 1. 方法的参数是函数

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210110203813.png" style="zoom:33%;" />

### 2. 方法的返回是函数

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210110204934.png" style="zoom:33%;" />

#### 调用方法

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210110205039.png" style="zoom:33%;" />



### 3. 方法的参数和返回值都是函数

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210112201902.png" style="zoom:33%;" />

## 柯里化函数（高阶函数）

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210112202913.png" style="zoom:33%;" />


# 4、Scala collection

## 字符串

字符串采用的就是 `java.lang.String`, 用法和 java 基本相同

```Scala
package com.coachhe.scala

object StringAndArray {
  def main(args: Array[String]): Unit = {
    /**
     * 字符串
     * 采用的就是java.lang.String
     * 用法和java基本相同
     */
    val s = "Hello"
    val s1 = "hello"

    println(s)
    println(s1)
    println(s.equals(s1))
    println(s.equalsIgnoreCase(s1))
    println(s.indexOf('H'))

  }

}

```

## 集合

集合分为可变集合和不可变集合

```scala
package com.coachhe.scala

object StringAndArray {
  def main(args: Array[String]): Unit = {

    /**
     * 集合
     */
      //定义方法1
    val arr = Array[String]("a","b","c","d");
    //遍历方法1
    arr.foreach(println)
    //遍历方法2
    for(elem <- arr) {
      println(elem)
    }
    //定义方法2  默认的定义方式都是不可变的
    val arr1 = new Array[Int](3)
    //赋值方法
    arr1(0) = 1
    arr1(1) = 1
    arr1(2) = 2
    // 定义2维数组
    val arr2 = new Array[Array[Int]](2)
    arr2(0) = Array[Int](1,2,3)
    arr2(1) = Array[Int](4,5,6)
    //打印2维数组
    for (arr <- arr2) {
      for (elem <- arr) {
        println(elem)
      }
    }
    //第2种方式打印2维数组
    for (arr <- arr2; elem <- arr) {
      println(elem)
    }
    //第3种方式打印2维数组
    arr2.foreach(arr=>{arr.foreach(println)})

    //数组的合并
    val arrays = Array.concat(arr1, arr2)
    //arrays.foreach(println)

    Array.fill(5)("Hello")
    
    //可变数组
    import scala.collection.mutable.ArrayBuffer
    val arr_mut = ArrayBuffer[Int](1,2,3)
    arr_mut.+=(4) //往后追加
    arr_mut.+=:(100)  //往前追加
    arr_mut.append(7,8,9) //往后追加

  }

}

```

## List

```scala
package com.coachhe.scala

import scala.collection.mutable.ListBuffer

object Lesson_List {
  /**
   * List
   */
  def main(args: Array[String]): Unit = {
    val list = List[Int](1,2,3)
    //遍历的两种方法
    list.foreach(println)
    for (elem <- list) {
      println(elem)
    }
    val list2 = List[String]("hello scala", "hello java")
    //map和flatmap
    val result: List[Array[String]] = list2.map(s => {
      s.split(" ")
    })
    result.foreach(arr => {
      arr.foreach(println)
    })
    val result2: List[String] = list2.flatMap(s => {
      s.split(" ")
    })
    result2.foreach(println)
    //filter 过滤掉不符合条件的内容
    val result3: List[String] = list2.filter(s => "hello scala".equals(s))
    result3.foreach(println)
    //count 计算符合条件的数量
    val result4: Int = list2.count(s => {
      s.length < 4
    })
    println(result4)

    //可变的list
    val list3 = ListBuffer[Int](1, 2, 3)
    list3.append(4,5,6)
    list3.+=:(100)
    list3.foreach(println)
    

  }

}

```

### map 方法和 flatmap 方法的区别

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210113011604.png" style="zoom:50%;" />



## Set

用法和List很像

### 方法

`intersect`

`diff`

```scala
package com.coachhe.scala

import scala.collection.immutable
import scala.collection.mutable

object Lession_Set {
  def main(args: Array[String]): Unit = {
    val set = Set[Int](1, 2, 3, 4, 4, 5)
    println("----")
    set.foreach(println)

    val set1 = Set[Int](3, 4, 5, 6)
    val result: Set[Int] = set1.intersect(set)  // 合并
    val result2: Set[Int] = set1.diff(set)      // 检查set1和set的不同之处(相当于是set1去掉set)
    val result3 = set1 & set1                   // 交集
    println("----")
    result.foreach(println)
    println("----")
    result2.foreach(println)
    println("----")
    result3.foreach(println)
    // 此外，filter等方法和List一样使用

    //可变的Set
    val set2 = mutable.Set[Int](1, 2, 3)
    val result4: set2.type = set2.+=(100)     //此时返回的是新的集合
    println("----")
    set2.foreach(println)
    set2.+=(100)                              //此时是往set2里新增元素
    println("----")
    result4.foreach(println)
    //重新定义一个不可变的Set
    val set3 = immutable.Set[String]("a","b","c")
  }
}

```

## Map

```scala
package com.coachhe.scala

import scala.collection.mutable
import scala.collection.immutable

object Lession_Map {
  def main(args: Array[String]): Unit = {
    //可以是"a" -> 100，也可以是("c",300)
    val map = Map[String, Int]("a" -> 100, "b" -> 200, ("c",300))
    map.foreach(println)
    val result1: Option[Int] = map.get("a")
    val result2: Int = result1.get
    println(result2)
    println(result2)
    // getOrElse方法
    val result3: Any = map.get("aa").getOrElse("no value")
        // 另一种方法
    val result4: Any = map.getOrElse("aa", "no value")
    println(result3)
    println(result4)
    // 获取所有的key
    val keys: Iterable[String] = map.keys
    // 获取所有value
      // 原理是遍历所有key时去get对应map的value
    println("---遍历---")
    keys.foreach(key => {
      println(s"key = $key, value = ${map.get(key)}")
    })
    println("---遍历方法2---")
    val values: Iterable[Int] = map.values
    values.foreach(println)

    // Map之间的操作
    val map1 = Map[String, Int]("a" -> 1, "b" -> 200)
    val map2 = Map[String, Int]("a" -> 100, "c" -> 300 )
      //map的合并1
    val result5: Map[String, Int] = map1.++(map2)  //map1和map2合并，并且用map2中的值替换map1的值
      //map的合并2
      val result6: Map[String, Int] = map1.++:(map2)  //map1和map2合并，并且用map1中的值替换map2的值
    println("---map1.++map2---")
    result5.foreach(println)   //此时a的value为100
    println("---map1.++:map2---")
    result6.foreach(println)   //此时a的value为100

    //可变map
    val map3 = mutable.Map[String, Int]()
    map3.put("a",1)
    map3.put("b",2)
    map3.put("c",3)
    println("--打印map3--")
    map3.foreach(println)
    // filter方法
    println("--filter--")
    val result7: mutable.Map[String, Int] = map3.filter(tp => {
      val key = tp._1
      val value = tp._2
      value == 200
    })
    result7.foreach(println)
  }
}
```

## 元祖（Tuple）

1. 元祖可以new也可以不new
2. 元祖最多支持22个元素
3. Tuple不能for和foreach
4. 取值用"_5"

```scala
package com.coachhe.scala

object Lession_Tuple {
  def main(args: Array[String]): Unit = {
    // 一堆元素放在一个括号里就是元祖
    // 和列表很像，只是放入了不同类型的元素
    val tuple1: Tuple1[String] = new Tuple1("Hello")
    val tuple2: (String, Int) = new Tuple2("a", 100)
    val tuple3: (Int, Boolean, Char) = new Tuple3(1, true, 'C')
      //也可以不用new
    val tuple4: (Int, Double, String, Boolean) = Tuple4(1, 3.4, "abc", false)
      //甚至可以不用Tuple
    val tuple5: (Int, Int, Int, Int, Int) = (1, 2, 3, 4, 5)
      //Tuple最多可以写到22个，23个时已经不是元祖了
    val tuple22: (Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int, Int) = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22)
    //数量为23时不能识别元素类型了，运行时会报错
    //val tuple23 = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)

    // 取元素
    println(tuple1._1)
    println(tuple2._2)
    println(tuple22._21)
    //遍历,元祖没有for和foreach
    println("--循环遍历1--")
    val iterator: Iterator[Any] = tuple5.productIterator
    iterator.foreach(println)
    println("--循环遍历2--")
    val iterator2: Iterator[Any] = tuple5.productIterator
    while (iterator2.hasNext) {
      println(iterator2.next())
    }
  }
}
```


# 五、Scala Trait、Match Case Class 和偏函数
## Trait

1. Trait 无法传参，没有括号

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210115014248.png" style="zoom:33%;" />

2. 当继承多个 Trait 时第一个关键字用 extends，后面全都是 with

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210115014225.png" style="zoom:33%;" />

3. 当类继承 Trait 时需要实现没有实现的方法

   <img src="/Users/heyizhi/Library/Application Support/typora-user-images/image-20210115014602817.png" alt="image-20210115014602817" style="zoom:33%;" />

   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210115014355.png" style="zoom:33%;" />

```scala
package com.coachhe.scala

object Lession_Trait {
  // 相当于接口和抽象类的整合
  def main(args: Array[String]): Unit = {

    val human = new Human()
    human.read("Zhang San")

    val point1 = new Point(1, 2)
    val point2 = new Point(1, 3)
    println(point1.isEqu(point2))
  }
}

trait Read {
  // trait无法传参，也就是没有括号
  def read(name:String) = {
    println(s"$name is reading...")
  }
}

trait Listen {
  def listen(name:String) = {
    println(s"$name is listening...")
  }
}

class Human() extends Read with Listen {
  // 继承多个trait时第一个关键字用extends,后面全都是with
}

trait IsEqual{
  def isEqu(o:Any):Boolean
  def isNotEqu(o:Any):Boolean = {
    !isEqu(o)
  }
}

class Point(x:Int, y:Int) extends IsEqual {
  val xx = x
  val yy = y

  override def isEqu(o: Any): Boolean = {
    o.isInstanceOf[Point] && o.asInstanceOf[Point].xx == this.xx
  }
}
```

## Match

1. case _ 什么默认匹配，放在最后一个
2. match 可以匹配值还可以匹配类型
3. 从上往下匹配，匹配上之后就不会往下匹配了
4. 匹配过程中会有数值的转换，例如Double转换为Int
5. 模式匹配中外部的"{}"可以省略

```scala
package com.coachhe.scala

/**
 * Match 模式匹配
 * 1. case _ 表示默认匹配，放在最后一个
 * 2. match 可以匹配值还可以匹配类型
 * 3. 从上往下匹配，匹配上之后就不会往下匹配了
 * 4. 匹配过程中会有数值的转换，例如Double转换为Int
 * 5. 模式匹配中外部的"{}"可以省略
 */

object Lession_Match {
  def main(args: Array[String]): Unit = {
    val tp = (1,1.0,"abc",'a', true)
    val iterator: Iterator[Any] = tp.productIterator
    iterator.foreach(MathTest)
  }


  def MathTest(o:Any): Unit = {
    o match {
      case i:Int=>println(s"type is Int, value = $i") //类型匹配，查看是否符合Int类型
      case d:Double=>println(s"type is Double, value = $d") //类型匹配，查看是否符合Double类型
      case s:String=>println(s"type is String, value = $s") //类型匹配，查看是否符合String类型
      case 1=> println("value is 1") //值匹配,查看值是否为1
      case 'a'=> println("value is a")
      case _=> println("no match") //相当于default，必须放在最后一个
    }
  }

}
```

### 输出

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210115020456.png" style="zoom:33%;" />

### 偏函数

如果一个方法没有match只有case，这个函数就可以定义成PartialFunction偏函数。

偏函数定义时，不能使用括号传参，默认定义PartialFunction中传入一个值，匹配上了对应的case，返回一个值

```scala
package com.coachhe.scala

/**
 * PartitionFunction 偏函数
 * 只能匹配一个值，匹配上了返回某个值
 * PartitionFunction[A,B]，A是匹配的类型，B是匹配上返回的类型
 */

object Lession_Match {
  def main(args: Array[String]): Unit = {
    val result: Int = PartialFunctionTest("abc")
    println(result)
  }

  def PartialFunctionTest : PartialFunction[String, Int] = {  //进来只能是String，出去是Int
    case "abc" => 2
    case "a" => 1
    case "100" => 200
    case _=> 0
  }
}
```

## 样例类

用case关键字定义的类就是样例类，样例类是种特殊的类，实现了类构造参数的getter方法（构造参数默认被声明为val），当构造参数是声明为var类型时，它将帮你实现setter和getter方法。

* 样例类帮你实现了toString，equals，copy和hashCode等方法

* 样例类可以new，也可以不new

```scala
package com.coachhe.scala

object Lession_CaseClass {
  def main(args: Array[String]): Unit = {
    val p1 = new Persion11("zhangsan", 10)
    val p2 = new Persion11("zhangsan", 10)
    println(p1.equals(p2))
    println(p1.hashCode())
    println(p1.toString)
  }
}
// 用case修饰的就是样例类
case class Persion11(var name:String, age:Int){
}

```

### 输出

<img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210115022015.png" style="zoom:33%;" />

# 六、Scala隐式转换

## 隐式转换定义

隐式转换是在 Scala 编译器进行类型匹配时，如果找不到合适的类型，那么隐式转换会让编译器在作用范围内自动推导出来合适的类型。

## 1. 隐式值与隐式函数

隐式值是指在定义参数时前面加上 implicit，隐式函数是指在定义方法时，方法中的部分参数是由 implicit 修饰。

#### 隐式转换作用

当调用方法时，不必手动传入方法中隐式参数，Scala 会自动在作用域范围内寻找隐式值自动传入

#### 注意

1. 如果在定义隐式函数时部分参数需要隐式指定，那么需要使用柯里化函数形式
   ```scala
     def sayName2(age:Int)(implicit name:String) = {
       println(s"name is $name, age is $age")
     }
   ```
2. 同类型的参数的隐式值只能在作用域内出现一次，同一个作用域内不能定义多个类型一样的隐式值
3. implicit 关键字必须放在隐式参数定义的开头
4. 一个方法只能有一个参数是隐式转换时，直接定义 implicit 关键字修饰的参数，调用时直接创建类型不传入参数即可
   <img src="https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210116024713.png" style="zoom:50%;" />

### 实例
![](https://coachhe.oss-cn-shenzhen.aliyuncs.com/Scala/20210116024414.png)

## 2. 隐式转换函数

### 定义

隐式转换函数是使用关键字implicit修饰的方法。当Scala运行时，假设如果A类型变量调用了method()这个方法，发现A类型的变量没有method()方法，而B类型有次method()方法，会在作用域中寻找有没有隐式转换将A类型转换成B类型，如果有隐式转换函数，那么A类型就可以调用method这个方法

### 注意

隐式转换函数只与函数的参数类型和返回类型有关，与函数名称无关，所以作用于内不能有相同的参数类型和返回类型的不同名称隐式转换函数

```scala
package com.coachhe.scala

class Animal(name:String){
  def canFly()={
    println(s"$name can fly")
  }
}

class Rabbit(xname:String){
  val name = xname
}

object Lesson_ImplicitTrans2 {
  
  // 隐式转换
  implicit def RabbitToAnimal(r:Rabbit): Animal = {
    // 分析：传进来一个Rabbit出去一个Animal
    new Animal(r.name)
  }
  
  def main(args: Array[String]): Unit = {
    val rabbit = new Rabbit("rabbit")    
    //此时就能调用rabbit没有的canFly方法了
    rabbit.canFly()
    
  }

}
```































