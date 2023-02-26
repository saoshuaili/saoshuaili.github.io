---
title: 补充-String和字符串常量池
tags: []
categories:
  - 编程语言
  - Java
  - Java语言
  - JVM
  - 第2章 Java内存区域与内存溢出异常
date: 2022-12-18 03:31:59
---

# 字符串常量池

要了解字符串常量池我们需要先知道运行时常量池，这里会在 [[2.3.2 虚拟机栈]]中进行详细介绍，这里我们就不展开了，主要来看字符串常量池。

字符串常量池中的内容是在类加载完成，经过验证、准备阶段之后存放在字符串常量池中。

## 字符串常量池存储位置的演进

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221218041521.png" width = "50%" />

可以看到，字符串常量池原本是属于运行时常量池的一部分，从 jdk7 开始被分割开来，分配到了堆空间中。

## 字符串常量池中存放的内容

这里其实是有比较大的争议的，有些人认为例如 `ab` 之类的字符串对象会创建在堆中，而字符串常量池只存一个引用，实际对象还是在堆中，例如：[从字符串到常量池，一文看懂String类设计](https://xie.infoq.cn/article/45250864e429630990703f7f3)

这篇文章所持有的观点就是字符串常量池只存堆中对象的引用。

很遗憾，这里我持有的观点和这篇文章是不同的，我认为，**字符串常量池中存放的就是实际的 String 对象**。

当然我是有证据的，具体的可以来看下 String 的 `intern()` 方法里面的定义：

```java
    /**
     * Returns a canonical representation for the string object.
     * <p>
     * A pool of strings, initially empty, is maintained privately by the
     * class {@code String}.
     * <p>
     * When the intern method is invoked, if the pool already contains a
     * string equal to this {@code String} object as determined by
     * the {@link #equals(Object)} method, then the string from the pool is
     * returned. Otherwise, this {@code String} object is added to the
     * pool and a reference to this {@code String} object is returned.
     * <p>
     * It follows that for any two strings {@code s} and {@code t},
     * {@code s.intern() == t.intern()} is {@code true}
     * if and only if {@code s.equals(t)} is {@code true}.
     * <p>
     * All literal strings and string-valued constant expressions are
     * interned. String literals are defined in section 3.10.5 of the
     * <cite>The Java&trade; Language Specification</cite>.
     *
     * @return  a string that has the same contents as this string, but is
     *          guaranteed to be from a pool of unique strings.
     * @jls 3.10.5 String Literals
     */
    public native String intern();
```

我们来挑选其中最重要的几句话来看看（其余的会在本章接下来介绍 intern 方法的时候具体展开）

- A pool of strings, initially empty, is maintained privately by the class {@code String}.

	这句话意思是字符串常量池是由 String 类私有的一个最开始为空的数据结构。
	
	在这句话中我们可以了解到，这个字符串常量池是 String 类私有的，并且应该是全局唯一的，因此所有类共用这个字符串常量池，但是每个 class 都有一个运行时常量池，因此运行时常量池的数量应该比字符串常量池更多。

- When the intern method is invoked, if the pool already contains a string equal to this {@code String} object as determined by the {@link # equals (Object)} method, then the string from the pool is returned. Otherwise, this  string object is added to the pool and a reference to this {@code String} object is returned.

	这句话的意思是当调用 `intern()` 方法时，如果运行时常量池中已经有了一个相等（equals）的字符串常量，那么直接返回这个 string 的引用。否则，这个 **string 对象 (this string object)** 会被加到字符串常量池中，并且返回引用。

	看到了吧，这个 **string 对象**！会被加载到字符串常量池中，而不是这个对象的引用会被加载到字符串常量池中，因此**字符串常量池放的是实际的对应而不是引用**！



# 定义 String 的方式

定义 String 有两种方式：

```java
String str = "abc";
String str_new = new String("def");
```

这两种定义有什么区别呢？我们可以通过字节码来看一下：

```java
 0 ldc #4 <abc>
 2 astore_1
 3 new #5 <java/lang/String>
 6 dup
 7 ldc #6 <def>
 9 invokespecial #7 <java/lang/String.<init> : (Ljava/lang/String;)V>
12 astore_2
13 return
```

## `String str = "abc"` 对应的操作

序号和字节码中的序号保持一致

0. 使用 `ldc` 将常量池中字符串 `abc` 的引用放入操作数栈中，注意，在这里 `ldc` 会触发对其符号引用的解析操作，具体可以看 [[7.3 类加载的过程]]，这里会创建一个字符串 `abc` 对应的对象并将其放入字符串常量池中。
2. 将操作数栈顶的元素（也就是 `abc`）放入局部变量表的第一个位置（第 0 个是 this）。

至此，`String str = "abc"` 已经结束，`str` 是一个引用，指向栈中的字符串 `abc`

## `String str = new String("def")` 对应的操作

同样，序号和字节码中的序号保持一致

3. 创建了一个 String 对象，保留了一个引用，结果为：def
6. 将这个引用复制一份，也就是保留了两个引用，结果为：def, def
7. 和第 0 行类似，使用 `ldc` 将常量池中指定的常量 `def` 放入操作数栈中
9. 使用栈顶元素（也就是 `def`）参与构造器方法的的初始化，这样消耗了栈顶的 `def` 和 `dup` 得到的引用，值保留一个引用，指向的是堆中的对象。
12. 将这个指向堆中对象的引用放入局部变量表的第二个位置。

可以看到，两种不同方式创建得到的引用虽然表面上看是很像的，但实际上确实存在了很大的差别。最重要的就是第一个引用指向的是栈，第二个引用指向的是堆

# 字符串拼接

所谓字符串拼接就是两个字符串或者字符串和对象相加

在进行之前我们先了解一下拼接的规则：

## 拼接的规则

1. 常量和常量的拼接结果在常量池，原理是编译器优化
2. 常量池中不会存在相同内容的变量
3. **只要其中有一个是变量，结果就在堆中**。变量拼接的原理是 `StringBuilder`
4. 如果拼接的结果调用 `intern()` 方法，则主动将常量池中还没有的字符串对象放入池中，并返回此对象地址

让我们慢慢来看，首先是上面第一条 : 常量和常量的拼接结果在常量池

```java
    // 证明常量拼接之后会通过编译器间优化放入常量池中
    @Test
    public void ConcatenationTest1(){
        String a = "abc";
        String b = "a" + "b" + "c";
        /**
         * 最终，java编译成.class，再执行.class
         * String a = "abc";
         * String b = "abc";
         */
        System.out.println(a == b); //true
        System.out.println(a.equals(b)); //true
    }
```

可以看到，判断 `a == b` 时输出的是 `true`，这是因为在编译期进行优化，可以看下编译之后的 class 文件反编译的结果：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221215024246.png" width = "70%" />

可以看到，编译期间进行了优化。

再看看第 3 条：**只要其中有一个是变量，结果就在堆中**。

```java
// 证明拼接时只要有一个变量，结果就会放入堆中  
@Test  
public void ConcatenationTest2(){  
    String s1 = "javaEE";  
    String s2 = "hadoop";  
  
    String s3 = "javaEEhadoop";  
    String s4 = "javaEE" + "hadoop";  
    String s5 = s1 + "hadoop";  
    String s6 = "javaEE" + s2;  
    String s7 = s1 + s2;  
  
    System.out.println(s3 == s4); // true  
    System.out.println(s3 == s5); // false  
    System.out.println(s3 == s6); // false  
    System.out.println(s3 == s7); // false  
    System.out.println(s5 == s6); // false  
    System.out.println(s5 == s7); // false  
    System.out.println(s6 == s7); // false  
  
    String s8 = s6.intern();  
    System.out.println(s3 == s8); // true  
}
```

可以看到，只要里面有个变量存在，这里都是会 new 一个对象，那么结果就不可能相同了。

但是最后调用 `intern` 方法之后，会将其放入字符串常量池，那么此时 `s3` 和 `s8` 就指向同一个对象了。

那么原理是 `StringBuilder` 怎么理解呢？

来看看下面这段代码：

```java
/**  
 * @author CoachHe  
 * @date 2022/12/15 02:37  
 **/
public class Concatenation {  
    public static void main(String[] args) {  
        String mango = "mango";  
        String s = "abc" + mango + "def" + 47;  
        System.out.println(s);  
    }  
}
```

将其进行反汇编：

```java
 0 ldc #14 <mango>
 2 astore_1
 3 new #9 <java/lang/StringBuilder>
 6 dup
 7 invokespecial #10 <java/lang/StringBuilder.<init> : ()V>
10 ldc #2 <abc>
12 invokevirtual #11 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
15 aload_1
16 invokevirtual #11 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
19 ldc #15 <def>
21 invokevirtual #11 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
24 bipush 47
26 invokevirtual #16 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
29 invokevirtual #12 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
32 astore_2
33 getstatic #3 <java/lang/System.out : Ljava/io/PrintStream;>
36 aload_2
37 invokevirtual #17 <java/io/PrintStream.println : (Ljava/lang/String;)V>
40 return
```

进行分析，我们可以看到，首先从字符串常量池的第 14 个位置获取字符串常量 `mango` ，并将其放入本地变量表的第一个位置，注意，这里第 0 个位置是默认的输入参数 `args`，因为是静态方法所以没有 `this` 变量，具体的可以看 JVM 内容 [[8.2.2 局部变量表]]。

```java
 0 ldc #14 <mango>
 2 astore_1
```

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20221215130047.png" width = "50%" />

然后 new 了一个 `StringBuilder` :

```java
 3 new #9 <java/lang/StringBuilder>
 6 dup
 7 invokespecial #10 <java/lang/StringBuilder.<init> : ()V>
```

具体细节可以看 [[7.3 类加载的过程]]

接着会从局部变量表中位置为 1 的方法加载进来。

```java
15 aload_1
```

然后调用 `StringBuilder` 的 `append` 方法，将栈帧顶部的局部变量表中的字符串 `mongo` append 进这个 `StringBuilder` 中，接着步骤也是相同的

```java
16 invokevirtual #11 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
19 ldc #15 <def>
21 invokevirtual #11 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
24 bipush 47
26 invokevirtual #16 <java/lang/StringBuilder.append : (I)Ljava/lang/StringBuilder;>
```

最后调用 `StringBuilder` 的 `toString` 方法，返回一个 `String` 对象，最后返回

```java
29 invokevirtual #12 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
32 astore_2
33 getstatic #3 <java/lang/System.out : Ljava/io/PrintStream;>
36 aload_2
37 invokevirtual #17 <java/io/PrintStream.println : (Ljava/lang/String;)V>
40 return
```

这就是代码的字节码层面的全部经过

所以这段代码等价于：

```java
String mango = "mango";  
StringBuilder s = new StringBuilder();  
s.append("abc");  
s.append(mango);  
s.append("def");  
s.append(47);  
System.out.println(s.toString());
```

注意：
- 字符串拼接操作时使用的不一定是 `StringBuilder`，如果拼接符号左右都是字符串常量或者常量引用，那么还是会使用编译器优化，结果放在字符串常量池中。
- 针对于 `final` 修饰类、方法和基本数据类型、引用数据类型的量的结构时，能使用 `final` 则尽量使用上

看下这个例子：

```java
// 为了说明用final声明的变量重载时不会自动使用new创建  
@Test  
public void ConcatenationTest3(){  
    final String s1 = "a";  
    final String s2 = "b";  
    String s3 = "ab";  
    String s4 = s1 + s2;  
    System.out.println(s3 == s4);//true  
}
```

可以看到，最终输出的还是 true，说明 s4 还是直接指向了字符串常量池。


## 拼接的效率

具体效率的比较可以参考[[2. 重载 + 与StringBuilder]]


# intern 方法

## 官方解释

Returns a canonical representation for the string object.
A pool of strings, initially empty, is maintained privately by the class String.
When the intern method is invoked, if the pool already contains a string equal to this String object as determined by the equals (Object) method, then the string from the pool is returned. Otherwise, this String object is added to the pool and a reference to this String object is returned.
It follows that for any two strings s and t, `s.intern() == t.intern()` is true if and only if `s.equals(t)` is true.
All literal strings and string-valued constant expressions are interned. String literals are defined in section 3.10.5 of the The Java™ Language Specification.
Returns:
a string that has the same contents as this string, but is guaranteed to be from a pool of unique strings.

简单来说，就是调用 `intern()` 方法的时候，如果字符串常量池中有一个调用 `equals` 方法返回 `true` 的字符串，那么会直接返回字符串常量池中的字符串。否则会将 `string` 对象放入字符串常量池，然后将 `reference` 返回。

并且当前仅当 `s.equals(t)` 时 `s.intern() == t.intern()`

## `intern()` 的使用（jdk6 vs jdk7/8）

如果不是用双引号声明的 `String` 对象，可以使用 `String` 提供的 `intern` 方法： `intern` 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中。

也就是说，如果在任何字符串上调用 `String.intern()` 方法，那么其返回结果所指向的那个类实例，必须和直接以常量形式出现的字符串实例完全相同。

## `new String` 时会创建几个对象

这个问题其实可以分为两个子问题：

1.  `new String("ab")` 会创建几个对象
2. `new String("a") + new String("b")` 会创建几个对象

首先来看第一个问题：

###  `new String("ab")` 会创建几个对象

这个问题其实应该很简单，一定会创建两个
1. 在字符串常量池中放入一个 `"ab"` 字符串
2. `new` 的这个对象，在堆中。

那么深入思考一下，你怎么证明这件事呢？

答案： 看**字节码**！

这里我们先看源代码：

```java
/**  
 * @author CoachHe  
 * @date 2022/12/16 00:04  
 **/public class NewString {  
    public static void main(String[] args) {  
        String str = new String("ab");  
    }  
}
```

非常简单，就是 `new` 了一个 `String` 类型变量。那么来看他的字节码：

```java
 0 new #2 <java/lang/String>
 3 dup
 4 ldc #3 <ab>
 6 invokespecial #4 <java/lang/String.<init> : (Ljava/lang/String;)V>
 9 astore_1
10 return
```

这里在 [[1. 字符串概述]]对其进行过了比较详细的分析，仅就创建对象而言，一共有两个步骤创建了对象。

0.  `new` 了一个 `String` 对象
4. 将 `#3` 指向的字符串常量的引用加入到栈顶中，在这里若 `#3` 指向的字符串常量没有被加载到字符串常量池中，则会创建一个常量池中的对象。

因此若是 `#3` 指向的字符串常量没有被加载过，那么一共就会创建两个对象，如果已经有对象被创建了，那么就只会创建 1 个对象。


使用 `ldc` 将常量池中字符串 `abc` 的引用放入操作数栈中，注意，在这里 `ldc` 会触发对其符号引用的解析操作，具体可以看 [[7.3 类加载的过程]]，这里会创建一个字符串 `abc` 对应的对象并将其放入字符串常量池中。

那么此时我们可以来回答第二个问题：

### `new String("a") + new String("b")` 会创建几个对象

同样的，我们可以来看它的字节码：

源代码同样很简单：

```java
/**
 * @author CoachHe
 * @date 2022/12/16 00:04
 **/
public class NewString {
    public static void main(String[] args) {
//        String str = new String("ab");
        String str = new String("a") + new String("b");
    }
}
```

然后看其字节码：

```java
 0 new #2 <java/lang/StringBuilder>
 3 dup
 4 invokespecial #3 <java/lang/StringBuilder.<init> : ()V>
 7 new #4 <java/lang/String>
10 dup
11 ldc #5 <a>
13 invokespecial #6 <java/lang/String.<init> : (Ljava/lang/String;)V>
16 invokevirtual #7 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
19 new #4 <java/lang/String>
22 dup
23 ldc #8 <b>
25 invokespecial #6 <java/lang/String.<init> : (Ljava/lang/String;)V>
28 invokevirtual #7 <java/lang/StringBuilder.append : (Ljava/lang/String;)Ljava/lang/StringBuilder;>
31 invokevirtual #9 <java/lang/StringBuilder.toString : ()Ljava/lang/String;>
34 astore_1
35 return
```

可以看到，首先是 `new` 了一个 `StringBuilder`，接着 `new` 了字符串 `"a"` 对应的 ` String ` 对象（此时还会创建一个字符串常量池中对应的 `"a"` 对象，将其放入字符串常量池中），` StringBuilder ` 调用了 ` append ` 方法，然后 ` new ` 了字符串 `"b"` 对应的 ` String ` 对象 (同理，这里和上一个字符串一样的操作)，然后再次调用了 ` StringBuilder ` 的 ` append ` 方法，最后调用了 ` StringBuilder ` 的 ` toString () ` 方法再 ` new ` 了一个返回的 ` String ` 对象。

因此一共有 6 个对象被创建了
1. `new` 的 `StringBuilder()`
2. `new` 的 `String()`
3. 常量池中的 `"a"`
4. `new` 的 `String()`
5. 常量池中的 `"b"`
6. `toString()` 方法 `new` 的一个 `String` 对象，也就是 `new String("ab")`

注意，执行完之后，字符串常量池中没有 `"ab"` 字符串，同样可以看字节码得到，因为没有 `ldc ab` 的操作

## 一个面试题

```java
/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2022/12/15
 * Time: 20:09
 * Description:
 */
public class Intern {
    public static void main(String[] args) {
        String s = new String("1");
        s.intern(); // 调用此方法之前字符串常量池中已经存在1
        String s2 = "1";
        System.out.println(s == s2); // false

        String s3 = new String("1") + new String("2"); // 执行完之后字符串常量池中不存在"12"，和jdk版本无关
        s3.intern(); // jdk6： 在字符串常量池中生成新对象"11"， jdk7：在字符串常量池中生成堆中对象的引用
        String s4 = "12"; // s4变量记录的地址：使用的上一行代码执行时，在常量池中生成的"11"的地址
        System.out.println(s3 == s4); // jdk6及以前:false, jdk7及以后:true
    }
}
```

上半段代码很容易理解，首先创建了一个堆中的对象，并且在字符串常量池中创建了字符串常量 `"1"`，因此在执行第二行 `s.intern()` 时，这里不会有什么效果，然后 `String s2 = "1"` 时创建了字符串常量池中变量 `"1"` 的引用 `s2`，这个和堆中的对象 `s` 显然没有什么关系，因此直接输出 false，这里无论是何种版本的 jdk 都是相同的结果。

难点在第二段代码， 这里有一个点需要注意，就是当我们使用 jdk6 以及之前的版本执行时，这里两个都会返回 false，但是在 jdk7 以及之后，这里都是第一个为 false，第二个为 true。

这是因为 jdk7 之后对这里做了一个优化。

jdk6 以及之前，执行 `s3.intern()` 时会在字符串常量池中创建一个 `"12"` 对象，然后直接返回，因此接下来执行 `String s4 = "12"` 时创建了一个字符串常量池中变量 `"12"` 的引用 `s4`，这个引用和 `s3` 显然也没有什么关系，因此会输出 false。

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221216003637.png" width = "70%" />

但是到了 jdk7 之后执行 `s3.intern()` 之后不会在字符串常量池中创建对应的字符串常量，而是会创建一个 `reference`，指向堆中 `new String("12")` 的对象。因此接下来 `s4` 也会指向堆中的对象，因此输出了 true。

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/20221216004129.png" width = "70%" />

面试题的拓展:

```java
// 何时执行intern方法的影响  
@Test  
public void internTest2(){  
    String s3 = new String("1") + new String("8");  
    String s4 = "18";  
    s3.intern();  
    System.out.println(s3 == s4); // false  
}
```

可以看到，和上面唯一的不同是 `String s4 = "18"` 往前提了一行。但是导致输出的结果变为 false。这是因为执行这行代码时字符串常量池中是没有 `"18"` 这个常量的，因此会创建并返回地址，那么此时和堆中的对象就完全没有关系了，再执行 `s3.intern()` 时因为字符串常量池中已经有了 `"18"`, 因此没有任何作用。

## 总结 String 中 `intern()` 方法的使用

jdk1.6 中，将这个字符串对象尝试放入常量池。 
- 如果常量池中有，并不会放入，返回已有的常量池中的对象的地址
- 如果没有，会把此对象**复制一份，放入常量池**，并返回常量池中的对象地址

jdk1.7 起，将这个字符串对象尝试放入常量池。
- 如果常量池中有，并不会放入，返回已有常量池中的对象的地址
- 如果没有，则会把**对象的引用地址复制一份，放入常量池**，并返回常量池中的引用地址

## 补充习题

习题 1：

```java
public void internTest3(){  
    String s = new String("a") + new String("b");  
  
	String s2 = s.intern(); // jdk6中：在常量池中创建一个字符串"ab"  
	                        // jdk7及以后：在常量池中没有创建字符串"ab",而是创建一个引用，指向new String("ab")，将此引用返回
  
    System.out.println(s2 == "ab"); // true  
    System.out.println(s == "ab"); // jdk1.6及以前: false, jdk1.7及以后：true
}
```

可以看到，和上面很类似，jdk1.6 以前是 false，我们就不多加赘述了，

在 jdk1.7 及以后，执行 `String s2 = s.intern()` 之后，会在字符串常量池中放入堆的引用并返回，因此 `s` 和 `s2` 指向的是同一个堆空间。

习题 2：

```java
    @Test  
    public void internTest4(){  
//        String s1 = new String("a") + new String("b");  
        String s1 = new String("ab");  
        s1.intern();  
        String s2 = "ab";  
        System.out.println(s1 == s2);  
    }
```

这里需要关注两种写法的不同，

```java
String s1 = new String("ab");
```

这种写法在字符串常量池中**会创建 `"ab"` 字符串常量**，因此执行 intern 是没有效果的，`s2` 会执行字符串常量池中的 `"ab"` 字符串常量。因此这里输出的是 false。

但是如果换成了这种写法：

```java
String s1 = new String("a") + new String("b");  
```

这种方法也会创建一个堆中的 `String` 对象，但是**不会在字符串常量池中创建字符串常量** `"ab"`，因此执行 `s1.intern()` 之后会在字符串常量池中创建一个指向堆中对象的引用，所以 `s2` 也是这个引用，因此这里会输出 true。

## intern 的效率测试

```java
    // 空间效率测试  
    @Test  
    public void internTest5(){  
        int MAX_COUNT = 1000 * 10000;  
        String[] arr = new String[MAX_COUNT];  
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};  
  
        long start = System.currentTimeMillis();  
        for (int i = 0; i < MAX_COUNT; i++) {  
            arr[i] = new String(String.valueOf(data[i % data.length])).intern();  
//            arr[i] = new String(String.valueOf(data[i % data.length]));  
        }  
        long end = System.currentTimeMillis();  
        System.out.println("花费的时间为: " + (end - start));  
  
        try {  
            Thread.sleep(1000000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }
```

通过比较可以得到，使用 `intern()` 方法可以大量减少生成的对象。



