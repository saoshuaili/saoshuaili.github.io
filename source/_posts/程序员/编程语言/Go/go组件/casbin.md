---
title: casbin  
date: 2022-12-04 02:30:07  
tags: []  
categories:
  - 编程语言
  - Go
  - go组件
---
# Golang最强⼤的访问控制框架casbin全解析

Casbin是⼀个强⼤的、⾼效的开源访问控制框架，其权限管理机制⽀持多种访问控制模型。⽬前这个框架的⽣态已经发展的越来越好了。提供了各种语⾔的类库，⾃定义的权限
模型语⾔，以及模型编辑器。在各种语⾔中，golang的⽀持还是最全的，所以我们就研究casbin的golang实现。


## 访问控制模型
### UGO(User, Group,Other)
这个是Linux中对于资源进⾏权限管理的访问模型。Linux中⼀切资源都是⽂件，每个⽂件都可以设置三种⾓⾊的访问权限（⽂件创建者，⽂件创建者所在组，其他⼈）。这种访
问模型的缺点很明显，只能为⼀类⽤户设置权限，如果这类⽤户中有特殊的⼈，那么它⽆能为⼒了。

### ACL(访问控制列表)
它的原理是，每个资源都配置有⼀个列表，这个列表记录哪些⽤户可以对这项资源进⾏CRUD操作。当系统试图访问这项资源的时候，会⾸先检查这个列表中是否有关于当前⽤
户的访问权限，从⽽确定这个⽤户是否有权限访问当前资源。linux在UGO之外，也增加了这个功能。

在linux系统中，我们可以使用getfacl和setfacl命令对某个资源设置增加某个人或者某个组的权限列表。操作系统会根据这个权限列表进行判断，当前用户是否有权限操作这个资源。

```shell
[root@VM-11-161-centos ~/download_data]# setfacl -m user:coachhe:rw- ./1.txt 
[root@VM-11-161-centos ~/download_data]# getfacl 1.txt 
# file: 1.txt
# owner: root
# group: root
user::rw-
user:coachhe:rw-
group::r--
mask::rw-
other::r--
```

可以看到，我们通过setfacl给对1.txt这个资源设置增加了coachhe用户的权限读写权限，然后通过getfacl命令可以看到用户的权限

## RBAC(基于角色的权限访问控制)
这个是很多业务系统最通⽤的权限访问控制系统。它的特点是在⽤户和具体权限之间增加了⼀个⾓⾊。就是先设置⼀个⾓⾊，⽐如管理员，然后将⽤户关联某个⾓⾊中，再将⾓
⾊设置某个权限。⽤户和⾓⾊是多对多关系，⾓⾊和权限是多对多关系。所以⼀个⽤户是否有某个权限，根据⽤户属于哪些⾓⾊，再根据⾓⾊是否拥有某个权限来判断这个⽤户
是否有某个权限。
RBAC的逻辑有更多的变种:
### 变种一：角色引入继承 
角色引入了继承概念，那么继承的角色有了上下级或者等级关系。 
### 变种二：角色引入了约束 
角色引入了约束概念。约束概念有两种，
#### 1. 静态职责分离： 
a、互斥角色：同一个用户在两个互斥角色中只能选择一个 
b、基数约束：一个用户拥有的角色是有限的，一个角色拥有的许可也是有限的 
c、先决条件约束：用户想要获得高级角色，首先必须拥有低级角色 
#### 2. 是动态职责分离： 
可以动态的约束用户拥有的角色，如一个用户可以拥有两个角色，但是运行时只能激活一个角色。 
### 变种三：既有角色约束，又有角色继承 
就是前面两种角色变种的集合。

## ABAC(基于属性的权限验证) 
Attribute-based access control,这种权限验证模式是用属性来标记资源权限的。
比如k8s中就用到这个权限验证方法.比如某个资源有pod属性,有命名空间属性,那么我设置的时候可以这样设置: 

```shell
Bob可以在命名空间 projectCaribou中读取pod: 
{"apiVersion": "abac.authorization.kubernetes.io/v1beta1", "kind": "Policy", "spec": {"user": "bob", "namespace": "projectCaribou", "resource": "pods", "readonly": true}} 
```

这个权限验证模型的好处就是扩展性好,一旦要增加某种权限,就可以直接增加某种属性.

# casbin的基本使用
casbin使用配置文件来设置访问控制模型。我们可以通过casbin的模型编辑器来查看

它有两个配置文件，model.conf和policy.csv。其中model.conf存储的是我们访问控制模型，policy.csv存储的是我们具体的用户权限配置。
例如：
<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20220517113604.png" width = "50%" />

使用示例：
```go
package main

import (
  "fmt"
  "log"

  "github.com/casbin/casbin/v2"
)

func check(e *casbin.Enforcer, sub, obj, act string) {
  ok, _ := e.Enforce(sub, obj, act)
  if ok {
    fmt.Printf("%s CAN %s %s\n", sub, act, obj)
  } else {
    fmt.Printf("%s CANNOT %s %s\n", sub, act, obj)
  }
}

func main() {
  e, err := casbin.NewEnforcer("./model.conf", "./policy.csv")
  if err != nil {
    log.Fatalf("NewEnforecer failed:%v\n", err)
  }

  check(e, "dajun", "data1", "read")
  check(e, "lizi", "data2", "write")
  check(e, "dajun", "data1", "write")
  check(e, "dajun", "data2", "read")
}```

```go
e, err := casbin.NewEnforcer("./model.conf", "./policy.csv")
```

主要看这一行，通过两个配置文件创建了一个Enforcer。

当然，casbin 可以读取具体 policy 的时候不仅仅可以通过 csv ⽂件进⾏读取，也可以通过数据库进⾏读取。这样我们甚⾄可以写⼀个⽤户管理后台来配置不同的⽤户权限。
model.conf 也是可以从配置⽂件中获取，也可以从代码中获取，从代码中获取就可以扩展为先读取数据库，再代码加载。但是 model.conf ⼀旦修改，对应的 policy 就需要进⾏
同步修改，所以 model 在⼀个系统中不要进⾏频繁修改。

## PML
在看完上面两个配置文件`./model.conf`和`./policy.csv`时候是不是一脸懵？要了解配置文件，我们需要先了解一下PML语言：
PML(PERM modeling language)。其中的 PERM 指的是 Policy-Effect-Request-Matcher 。

### Request
代表请求。看我们上面的例子：
```pml
[request_definition]
r = sub, obj, act
```

代表一个请求有三个标准元素，请求主体，请求对象，请求操作
### Policy和Policy_Rule
Policy 代表策略，它表示具体的权限定义的规则是什么
在policy.csv文件中定义的策略就是policy_rule。它和Policy是一一对应的。
例如我们上面的例子：
```pml
[policy_definition]
p = sub, obj, act
```

我们定义了 policy 的规则如此，那么我们在 policy.csv 中每⼀⾏定义的 policy_rule 就必须和这个属性⼀⼀对应。

<img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/20220517114718.png" width = "50%" />
可以看到，policy.csv和policy_definition中的属性一一对应，所以在我们这里，sub=dajun,obj=data1,act=read，代表dajun(p.sub=dajun)可以对data1(p.obj=data1)进行read(p.act=read)操作这个规则。
因此dajun can read data1

policy默认的最后一个属性为决策结果，字段名为eft，默认值为allow，也就是说，通过的情况下，p.eft就设置为allow

### Matcher

有请求，有规则，那么请求是否匹配某个规则，则是matcher进行判断的
比如我们这里这个：
```pml
[matchers]
m = r.sub == p.sub && r.obj == p.obj && r.act == p.act
```

表示当`r.sub == p.sub && r.obj == p.obj && r.act == p.act`的时候返回true，否则返回false

### Effect
Effect ⽤来判断如果⼀个请求满⾜了规则，是否需要同意请求。它的规则⽐较复杂⼀些。

比如我们这里：
```pml
[policy_effect]
e = some(where (p.eft == allow))
```

这里的some表示括号中的表达式个数大于等于1就行。
我们这句话的意思就是将request和所有policy比对完之后，所有policy的策略结果(p.eft)为allow的个数>=1，整个请求的策略就是true

## 自定义函数
⾃定义函数是在 matcher 中使⽤的。我们可以⾃⼰定义⼀个函数，然后注册进enforcer，在matcher中我们就可以使⽤了。

```go
func KeyMatch(key1 string, key2 string) bool {
	// i用来看*在key2中的哪个位置
    i := strings.Index(key2, "*")
	// 如果key2中没有*，那么直接比较key1和key2
    if i == -1 {
        return key1 == key2
    }
    
    if len(key1) > i {
        return key1[:i] == key2[:i]
    }
    return key1 == key2[:i]
}
func KeyMatchFunc(args ...interface{}) (interface{}, error) {
    name1 := args[0].(string)
    name2 := args[1].(string)
    return (bool)(KeyMatch(name1, name2)), nil
}
e.AddFunction("my_func", KeyMatchFunc)
```

在这里我们定义了一个函数KeyMatchFunc，比较key1和key2，然后将其注册进了e中，这样我们就可以在matcher中直接使用了：

```pml
// 配置⽂件中就可以这样写了
[matchers]
m = r.sub == p.sub && my_func(r.obj, p.obj) && r.act == p.act
```


### casbin中的自定义函数

| Function   | arg1                                       | arg2                                                   |
| ---------- | ------------------------------------------ | ------------------------------------------------------ |
| KeyMatch   | a URL path like `/alice_data/resource1`    | a URL path or a `*` like `/alice_data/*`               |
| KeyMatch2  | a URL path like `/alice_data/resource1`    | a URL path or a `:` like `/alice_data/:resource`       |
| KeyMatch3  | a URL path like `/alice_data/resource1`    | a URL path or a `{}` like `/alice_data/{resource}`     |
| KeyMatch4  | a URL path like `/alice_data/123/book/123` | a URL path or a `{}` like `/alice_data/{id}/book/{id}` |
| regexMatch | any sthing                                 | a regular expression pattern                           |
| ipMatch    | an IP address like  `192.168.2.123`        | an IP address or a CIDR like `192.168.2.0/24`          |


## 部分理解
1 我们先定义属性，通⽤的⼀些属性如 subject, object, action。
2 定义的属性可以作为 Request 的属性，也可以作为 Policy的属性。
3 Policy_Rule 是 Policy 的具体规则。
4 使⽤定义的 Matcher 将 Request 和 Policy 进⾏匹配，这个匹配的过程可能使⽤到⾃定义函数。
5 所有的 Policy 匹配完成的结果，通过 Effect 规则得出最终是否可以访问的结果。


## 例子
### ACL
```yml
[request_definition]  
r = sub, obj, act  
  
[policy_definition]  
p = sub, obj, act  
  
[policy_effect]  
e = some(where (p.eft == allow))  
  
[matchers]  
m = r.sub == p.sub && r.obj == p.obj && r.act == p.act || r.sub == "root"
```

Request:
```shell
alice, data1, read
```

这里例子中定义一个Policy_Rule：alice对data1有read权限，当request(alice,data1,read)进来时，它匹配其中一条规则，所以some之后的最终结果为true

### RESTFUL
RESTFUL接口使用URL和HTTP请求方法表示资源的增删改查，那么我们可以用KeyMatch等函数来判断是否可以进行某个请求

```yml
[request_definition]  
r = sub, obj, act  
  
[policy_definition]  
p = sub, obj, act  
  
[policy_effect]  
e = some(where (p.eft == allow))  
  
[matchers]  
m = r.sub == p.sub && (keyMatch2(r.obj, p.obj) || keyMatch(r.obj, p.obj)) && (r.act == p.act || p.act == "*")
```


Request:
```shell
alice, /alice_data/hello, GET
```

当请求进来时，KeyMatch会判断alice对hello资源是否有GET权限，如果有则判断为true


# 总结
Casbin ⽀持的权限模型有：
* ACL (Access Control List, 访问控制列表)
* 具有超级⽤户的 ACL
* 没有⽤户的 ACL: 对于没有⾝份验证或⽤户登录的系统尤其有⽤。
* 没有资源的 ACL: 某些场景可能只针对资源的类型, ⽽不是单个资源, 诸如 write-article, read-log等权限。它不控制对特定⽂章或⽇志的访问。
* RBAC (基于⾓⾊的访问控制)
* ⽀持资源⾓⾊的RBAC: ⽤户和资源可以同时具有⾓⾊ (或组)。
* ⽀持域/租户的RBAC: ⽤户可以为不同的域/租户设置不同的⾓⾊集。
* ABAC (基于属性的访问控制): ⽀持利⽤resource.Owner这种语法糖获取元素的属性。
* RESTful: ⽀持路径, 如 `/res/*`, `/res/:id` 和 HTTP ⽅法, 如 `GET`, `POST`, `PUT`, `DELETE`。
* 拒绝优先: ⽀持允许和拒绝授权, 拒绝优先于允许。


