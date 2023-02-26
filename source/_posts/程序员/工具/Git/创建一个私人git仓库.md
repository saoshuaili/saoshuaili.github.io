---
title: 创建一个私人git仓库  
date: 2022-12-04 02:36:58  
tags: []  
categories:
  - 工具
  - Git
---
# 1. 在远端服务器上建立一个新的用户，专门用来存储 git 仓库
``` shell
$ useradd git
```



# 2. 在远端服务器上创建一个空的 git 仓库
```git
git init --bare <reponame>
```

执行结果：
```shell
[git@VM-4-16-centos ~]$ git init --bare coachhe-code
Initialized empty Git repository in /home/git/coachhe-code/
[git@VM-4-16-centos ~]$ ls
coachhe-code  company-working
[git@VM-4-16-centos ~]$ cd coachhe-code/
[git@VM-4-16-centos coachhe-code]$ ls
branches  config  description  HEAD  hooks  info  objects  refs
```

可以看到，创建了一个空的仓库。

# 3. 在本地将仓库同步过来

```shell
$ git clone git@43.154.34.118:/home/git/coachhe-code coachhe-code
Cloning into 'coachhe-code'...
warning: You appear to have cloned an empty repository.
```


