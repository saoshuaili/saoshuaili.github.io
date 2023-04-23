---
title: Spring简介
tags: []
categories:
  - 程序员
  - 编程语言
  - Java
  - Java开发
  - Java框架
  - Spring
date: 2022-12-04 02:29:40
---

# 1. Spring

## 1.0 介绍

Spring 是一款主流的 Java EE 轻量级开源框架。

- 框架：简单来说就是一个程序的半成品
- 开源：开放源代码
- 轻量级：体积小+不需要依赖其他组件

## 1.1 优点

* Spring是一个开源的免费的框架（容器）！
* Spring是一个轻量级的、非入侵式的框架！
* 控制反转（IOC），面向切面编程（AOP）
* 支持事务的处理，对**框架整合**的支持！

==总结一句话：Spring就是一个轻量级的控制反转（IOC）和面向切面编程（AOP）的框架！==







# 2. IOC理论推导

## 之前的实现方式

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210708000602515.png" width="50%">

为了方便把多个代码放在一起：

```java
// MyTest.java
import com.coachhe.spring.service.impl.UserServiceImpl;

public class MyTest {
    public static void main(String[] args) {

        // 用户实际调用的是业务层，DAO层他们不需要调用
        UserServiceImpl userService = new UserServiceImpl();
        userService.getUser();
    }
}

// UserDao.java
package com.coachhe.spring.dao;

public interface UserDao {
    void getUser();
}

// UserDaoImpl.java
package com.coachhe.spring.dao.impl;

import com.coachhe.spring.dao.UserDao;

public class UserDaoImpl implements UserDao {
    public void getUser() {
        System.out.println("获取默认用户的数据");
    }
}

// UserService.java
package com.coachhe.spring.service;

public interface UserService{
    public void getUser();
}

// UserServiceImpl.java
package com.coachhe.spring.service.impl;

import com.coachhe.spring.dao.UserDao;
import com.coachhe.spring.dao.impl.UserDaoImpl;
import com.coachhe.spring.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    public void getUser() {
        userDao.getUser();
    }
}

```

在这里，UserDaoImpl是UserDao的实现类，实现的功能是获取默认用户的数据。

UserServiceImpl是UserService的实现类，实现的功能是调用UserDaoImpl，获取默认用户的数据（查）。

运行结果：

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210708001602430.png" alt="image-20210708001602430" style="zoom:50%;" />



现在新增一种Dao实现类

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210708001958090.png" alt="image-20210708001958090" style="zoom:50%;" />

新增一种通过mysql获取用户数据的实现类之后，用户希望通过myslq方式来获取数据，那么我们需要改动Service方法，也就是说，通过改变源代码来实现，这显然是一种糟糕的方式。

## 改变

因此，我们在UserServiceImpl中加入了set方法：

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210708002257119.png" width="50%">

加入set方法之后，我们就可以直接让用户来选择需要的获取数据方式，而不是通过源代码来实现。

<img src=" https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/%E7%A8%8B%E5%BA%8F%E5%91%98/%E5%B7%A5%E5%85%B7/git/image-20210708002438027.png" width="50%"/>

注意，这里是用户界面，利用set动态实现值的注入。

用户可以直接选择需要的获取数据方式，而不是需要我们被动去更改源代码。

# 3. Hello Spring

pojo中

```java
package com.hou.pojo;

public class Hello {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "name='" + name + '\'' +
                '}';
    }
}
```

resource中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--bean = 对象-->
    <!--id = 变量名-->
    <!--class = new的对象-->
    <!--property 相当于给对象中的属性设值-->
    
    <bean id="hello" class="com.hou.pojo.Hello">
        <property name="name" value="Spring"/>
    </bean>
</beans>
```

test

```java
import com.hou.pojo.Hello;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Mytest {

    public static void main(String[] args) {
        //获取spring上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //我们的对象下能在都在spring·中管理了，我们要使用，直接取出来就可以了
        Hello hello = (Hello) context.getBean("hello");
        System.out.println(hello.toString());
    }
}
```

bean = 对象
id = 变量名
class = new的对象
property 相当于给对象中的属性设值

核心用set注入

第一个文件中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userdaomysql" class="com.hou.dao.UserDaoMysqlImpl"></bean>

    <bean id="userServiceImpl" class="com.hou.service.UserServiceImp">
        <!--ref引用spring中已经创建很好的对象-->
        <!--value是一个具体的值-->
        <property name="userDao" ref="userdaomysql"/>
    </bean>

</beans>
```



# 4. IOC生成对象的方式

1. 使用无参构造创建对象，默认。
2. 使用有参构造

下标赋值

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="user" class="com.hou.pojo.User">
        <constructor-arg index="0" value="hou"/>
    </bean>
</beans>
```

类型赋值（不建议使用）

```xml
<bean id="user" class="com.hou.pojo.User">
    <constructor-arg type="java.lang.String" value="dong"/>
</bean>
```

直接通过参数名

```xml
<bean id="user" class="com.hou.pojo.User">
    <constructor-arg name="name" value="hou"></constructor-arg>
</bean>
```

Spring类似于婚介网站！

你想不想要，对象都在里面。注册bean之后用不用被实例化。



# 5. Spring配置说明

**别名**

```xml
<bean id="user" class="com.hou.pojo.User">
    <constructor-arg name="name" value="hou"></constructor-arg>
</bean>

<alias name="user" alias="user2aaa"/>
```

**Bean的配置**

- id：bean的id标识符
- class：bean对象所对应的类型
- name：别名，更高级，可以同时取多个别名。

**import**

一般用于团队开发，它可以将多个配置文件，导入合并为一个

```xml
<import resource="beans.xml"/>
<import resource="beans2.xml"/>
<import resource="beans3.xml"/>
```





























# 10. 代理模式

## 10.1 静态代理

### 角色分析：

* 抽象角色：一般会使用接口或者抽象类来解决
* 真实角色：被代理的角色
* 代理角色：代理真实角色，代理真实角色后，我们一般会做一些附属操作
* 客户：访问代理对象的人

### 代码步骤：

* 抽象角色（接口）

  ```java
  public interface Rent {
      void rent();
  }
  ```

* 真实角色

  ```java
  // 房东
  public class Host implements Rent {
      public void rent() {
          System.out.println("房东租房子");
      }
  }
  ```

* 代理角色

  ```java
  // 代理
  package com.coachhe.demo01;
  
  public class Proxy implements Rent{
      private Host host;
      public Proxy(){
      }
  
      public void seeHouse(){
          System.out.println("中介带你看房");
      }
  
      public Proxy(Host host) {
          this.host = host;
      }
  
      public void sign(){
          System.out.println("中介签合同");
      }
  
      public void rent() {
          seeHouse();
          System.out.println("中介带你去看房");
          sign();
      }
  }
  
  ```

* 客户

  ```java
  package com.coachhe.demo01;
  
  public class Client {
  
      public static void main(String[] args) {
          Host host = new Host();
  
          // 代理
          Proxy proxy = new Proxy(host);
  
          // 通过房东租房
          // host.rent();
  
          // 通过代理租房
          proxy.rent();
      }
  }
  
  ```

  

### 代理模式的好处：

* 可以使真实角色的操作更加纯粹，不用去关注一些公共业务
* 公共也就交给代理角色，实现和业务的分工
* 公共业务发生拓展的时候，方便集中管理

### 代理模式的缺点：

* 一个真实角色就会产生一个代理角色，代码量会翻倍，开发效率会变低

## 10.2 静态代理-加深理解

### 代码分析

* 抽象角色：

  ```java
  // 抽象类，实现增删改查
  public interface UserService {
      void add();
      void delete();
      void change();
      void search();
  }
  
  ```

* 真实角色：

  ```java
  // 真实的UserService，做增删改查的人
  public class UserServiceImpl implements UserService{
  
      public void add() {
          System.out.println("使用了add方法");
          System.out.println("增加了一个用户");
      }
  
      public void delete() {
          System.out.println("使用了delete");
          System.out.println("删除了一个用户");
      }
  
      public void change() {
          System.out.println("使用了change方法");
          System.out.println("修改了一个用户");
      }
  
      public void search() {
          System.out.println("使用了search方法");
          System.out.println("查询了一个用户");
      }
  }
  
  
  ```

* 代理角色：

  ```java
  // 代理
  public class UserServiceProxy implements UserService{
  
      private UserServiceImpl userService;
  
      public void setUserService(UserServiceImpl userService) {
          this.userService = userService;
      }
  
      public void add() {
          System.out.println("proxy的add方法");
          userService.add();
      }
  
      public void delete() {
          System.out.println("proxy的delete方法");
          userService.delete();
      }
  
      public void change() {
          System.out.println("proxy的change方法");
          userService.change();
      }
  
      public void search() {
          System.out.println("proxy的search方法");
          userService.search();
      }
  }
  
  ```

* 客户：访问代理对象的人

  ```java
  // 客户
  public class Client {
      public static void main(String[] args) {
          UserServiceImpl userService = new UserServiceImpl();
          // 使用代理
          UserServiceProxy userServiceProxy = new UserServiceProxy();
          userServiceProxy.setUserService(userService);
          userServiceProxy.add();
      }
  }
  ```

## 10.3 动态代理

* 动态代理和静态代理角色一样
* 动态代理的代理类是动态生成的，不是我们直接写好的
* 动态代理分为两大类：
  * 基于接口的动态代理---JDK动态代理
  * 基于类的动态代理：cglib
  * Java字节码实现：JAVAssist

### 需要了解两个类：

* Proxy：代理
* InvocationHandler：调用处理程序









































