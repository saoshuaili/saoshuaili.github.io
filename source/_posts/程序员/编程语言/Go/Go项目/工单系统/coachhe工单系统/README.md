---
title: README  
date: 2022-12-04 02:30:25  
tags: []  
categories:
  - 编程语言
  - Go
  - Go项目
  - 工单系统
  - coachhe工单系统
---
# 工单协议

## 通用协议

### 返回值

| 参数    | 类型   | 注释                                 |
| ------- | ------ | ------------------------------------ |
| code    | string | 返回代码， 成功：0， 其他失败        |
| message | string | 如果是失败，返回对应的错误信息       |
| data    | object | 返回的数据，可能是数组或者直接是对象 |

- demo

  ```json
  {
    "code": 0,
    "message": "success",
    "data": [
      {
        "id": 1,
        "taskUniqueId": "test1",
        "taskName": "test1",
      }
    ]
  }
  ```

- 失败demo

  ```json
  {
    "code": -1,
    "message": "taskUniqueId is duplicate!",
    "data": null
  }
  ```

  

## 角色

### 默认角色

默认角色有两种：管理员和游客。

1. 游客

   有权限查询工单，并且有权限处理当前处理人为自己的工单（但是无法结单），但是没有权限处理其余工单。

2. 管理员

   管理员有权限处理所有工单。

### 创建角色

管理员有权限创建角色，创建时可以选择需要赋予的权限

#### 接口

`/api/v1/role`

#### 方法

POST

#### 参数

| 参数名      | 参数类型 | 是否必须      | 参数描述                             | demo                                                |
| ----------- | -------- | ------------- | ------------------------------------ | --------------------------------------------------- |
| nowUserName | string   | Y             | 登录人用户名（必须为企业微信英文名） | coachhe                                             |
| roleId      | int      | N（默认递增） | 角色Id                               | 0(只有管理员是0，可以通过是否为0来判断是否为管理员) |
| roleName    | string   | Y             | 角色名称                             | 管理员                                              |
| roleKey     | string   | Y             | 角色使用代码                         | admin                                               |
| remark      | string   | N             | 备注                                 | 创建工单备注                                        |
| munuIds     | list     | Y             | 角色的菜单列表（也用来鉴权）         | [0,1,3]                                             |

### 菜单列表

这里列出几个demo：

| menu_id | menu_name            | title                               | icon       | path                                 | paths                 | menu_type | action | permission                      | parent_id | no_cache | breadcrumb | component                        | sort | visible | create_by | update_by | is_frame | create_time         | update_time         | delete_time |
|---------|----------------------|-------------------------------------|------------|--------------------------------------|-----------------------|-----------|--------|---------------------------------|-----------|----------|------------|----------------------------------|------|---------|-----------|-----------|----------|---------------------|---------------------|-------------|
|       2 | Upms                 | 系统管理                            | example    | /upms                                | /0/2                  | M         | 无     |                                 |         0 | 1        |            | Layout                           |   20 | 0       | 1         | 11        |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|       3 | Sysuser              | 用户管理                            | user       | sysuser                              | /0/2/3                | C         | 无     | system:sysuser:list             |         2 | NULL     | NULL       | /system/sysuser/index            |    1 | 0       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|      43 | NULL                 | 新增用户                            | NULL       | /api/v1/sysuser                      | /0/2/3/43             | F         | POST   | system:sysuser:add              |         3 | NULL     | NULL       | NULL                             |    0 | 0       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|      44 | NULL                 | 查询用户                            | NULL       | /api/v1/sysuser                      | /0/2/3/44             | F         | GET    | system:sysuser:query            |         3 | NULL     | NULL       | NULL                             |    0 | 0       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|      45 | NULL                 | 修改用户                            | NULL       | /api/v1/sysuser/                     | /0/2/3/45             | F         | PUT    | system:sysuser:edit             |         3 | NULL     | NULL       | NULL                             |    0 | 0       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|      46 | NULL                 | 删除用户                            | NULL       | /api/v1/sysuser/                     | /0/2/3/46             | F         | DELETE | system:sysuser:remove           |         3 | NULL     | NULL       | NULL                             |    0 | 0       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |
|      67 |                      | 菜单列表                            | tree-table | /api/v1/menulist                     | /0/63/66/67           | A         | GET    |                                 |        66 | 0        |            |                                  |    1 | 1       | 1         | 1         |        0 | 2020-07-26 21:51:44 | 2020-07-26 21:52:10 | NULL        |

需要重点注意的参数有几个：

1. title：描述了菜单内容
1. path：菜单对应的请求url
1. menu_type：菜单类型，若==类型为A==则需要插入到casbin规则表中

4. parent_id：对应的母菜单id，展示时可以使用层级模式

   <img src="https://coachhe-1305181419.cos.ap-guangzhou.myqcloud.com/Redis/image-20220616111628539.png" width = "30%" />

5. action：请求方式

   注意：path+action可以组成casbin规则，从而进行权限控制



### casbin介绍

用来做权限管理，简单可以用一个表来表示

| p_type | v0     | v1                                   | v2     | v3   | v4   | v5   | id  | create_time | update_time | delete_time |
|--------|--------|--------------------------------------|--------|------|------|------|-----|-------------|-------------|-------------|
| p      | admin  | /api/v1/menulist                     | GET    | NULL | NULL | NULL |   1 | NULL        | NULL        | NULL        |
| p      | admin  | /api/v1/menu                         | POST   | NULL | NULL | NULL |   2 | NULL        | NULL        | NULL        |
| p      | admin  | /api/v1/menu                         | PUT    | NULL | NULL | NULL |   3 | NULL        | NULL        | NULL        |
| p      | admin  | /api/v1/menu/:id                     | DELETE | NULL | NULL | NULL |   4 | NULL        | NULL        | NULL        |

可以看到，v0列是我们的角色名，v1列是我们对应的接口，v2列是接口方法，只有这样纪录的一条，我们在鉴权的时候才会判定通过，若没有对应的权限，则接口不允许访问。

### 请求demo

```shell
curl 'http://9.135.11.161:60780/api/v1/role' \
  -H 'Content-Type: application/json' \
  --data-raw '{"nowUserName":"coachhe","roleName":"游客","roleId":"tourist","menuIds":[269,328,329,361,270,342,343,350,271,341,370,272,340,273,337,338,339,364,256,258,260,267,259,80,344,92,94,142,252,254,255,326,327,330,331,332,360,333,334,335,336,351,363,371,268,63,281],"remark":"创建一个游客角色"}'
```



### 返回

#### 成功返回

```json
{
  "code": 200,
  "data": {
    "roleId": 12,
    "roleName": "测试工单fds",
    "status": "0",
    "roleKey": "fsadfasfsdf",
    "roleSort": 0,
    "createBy": "1",
    "updateBy": "",
    "remark": "",
    "admin": false,
    "params": "",
    "menuIds": [
      268,
      325
    ],
    "create_time": "2022-06-16T11:42:15.224502054+08:00",
    "update_time": "2022-06-16T11:42:15.224502054+08:00"
  },
  "msg": "添加成功"
}
```



#### 失败返回

```json
{
  "code": -1,
  "data": null,
  "msg": "角色名称或者角色标识已经存在！"
}
```



## 用户

用户和角色是多对一的关系，一个角色可以绑定多个用户，一个用户只能属于一个角色。

用户的权限和所属角色完全相同。

### 默认用户

默认情况下为游客，游客有权限查询工单，但是没有权限流转和结单。

管理员拥有所有权限，包括查询工单、流转工单、删除工单、结单、创建用户、创建角色、更改用户所属角色等。



### 创建用户

管理员有权限创建用户，创建时需要指定用户属于什么角色

#### 接口

`/api/v1/sysUser`

#### 方法

POST

#### 参数

| 参数名      | 参数类型 | 是否必须      | 参数描述                             | demo          |
| ----------- | -------- | ------------- | ------------------------------------ | ------------- |
| nowUserName | string   | Y             | 登录人用户名（必须为企业微信英文名） | coachhe       |
| userId      | int      | N（默认递增） | 用户Id                               | 1             |
| password    | string   | Y             | 用户密码                             | 123456        |
| phone       | string   | N             | 用户手机号                           | 13823282927   |
| roleId      | int      | Y             | 用户所属角色号                       | 0（管理员）   |
| sex         | string   | N             | 用户性别                             | male          |
| remark      | string   | N             | 备注                                 | 创建工单备注  |
| extends     | string   | N             | 拓展字段，以备不时之需               | {"aaa":"bbb"} |

#### 返回

```json
{
  "code": 200,
  "data": 8,
  "msg": "添加成功"
}
```











